// src/main/resources/static/js/tables/users.js

let dataTableInstance = null; // DataTables 인스턴스를 관리합니다.
let loadingSpinner = null;
let simpleSearchInput = null;
let simpleSearchButton = null;
let advancedSearchButton = null;
let resetButton = null;
let contentsWrapper = null;

// 모든 DOM 요소 변수를 초기화하는 함수
function initializeUsersDomElements() {
    contentsWrapper = document.getElementById('contents-wrapper');
    loadingSpinner = document.getElementById('loading-spinner');

    if (contentsWrapper) {
        simpleSearchInput = contentsWrapper.querySelector('.d-sm-inline-block input[type="text"]');
        simpleSearchButton = contentsWrapper.querySelector('.d-sm-inline-block .btn.btn-primary');
        advancedSearchButton = contentsWrapper.querySelector('#advanced-search-btn');
        resetButton = contentsWrapper.querySelector('#reset-btn');
    }
}

// DataTables를 초기화하는 함수
function initializeUsersTable() {
    if (!dataTableInstance) {
        dataTableInstance = $('#dataTable').DataTable({
            language: {
                zeroRecords: '데이터가 없습니다.'
            },
            info: false,
            searching: false,
            lengthChange: false,
            paging: true,
            order: []
        });
    }
}

/**
 * @param {string} method - 'GET' 또는 'POST'
 * @param {object|string|null} payload - GET 요청 시 userId, POST 요청 시 검색 조건 객체
 * @param {string} title - 테이블 제목으로 표시될 텍스트
 */
function loadTableData(method, payload, title) {
    document.getElementById('table-title').textContent = title;

    if (loadingSpinner) {
        loadingSpinner.style.display = 'block';
    }

    if (dataTableInstance) {
        dataTableInstance.clear().draw();
    }

    let fetchOptions = {
        method: method,
        headers: {
            'Authorization': sessionStorage.getItem('auth-token'),
            'userId': sessionStorage.getItem('user-id')
        }
    };
    let url = '';

    if (method === 'GET') {
        url = `http://localhost:8000/user-service/search/${payload.userId}`;
    } else if (method === 'POST') {
        url = 'http://localhost:8000/user-service/search';
        fetchOptions.headers['Content-Type'] = 'application/json';
        fetchOptions.body = JSON.stringify(payload);
    }

    fetch(url, fetchOptions)
        .then(response => {
            if (!response.ok) {
                throw new Error('네트워크 응답 오류 발생');
            }
            return response.json();
        })
        .then(data => {
            const users = (method === 'GET') ? (data ? [data] : []) : data;

            if (users && users.length > 0) {
                const tableRows = users.map((user, index) => {
                    const date = new Date(user.createdAt);
                    const formattedDate = date.toLocaleDateString('ko-KR', {
                        year: 'numeric',
                        month: '2-digit',
                        day: '2-digit'
                    }).replace(/\. /g, '.');

                    return [
                        dataTableInstance.rows().count() + index + 1,
                        user.userId,
                        user.name,
                        user.email,
                        formattedDate
                    ];
                });

                dataTableInstance.rows.add(tableRows).draw();

            } else {
                dataTableInstance.clear().draw();
            }
        })
        .catch(error => {
            console.error('테이블 데이터를 불러오는 데 실패했습니다:', error);
            alert('데이터를 불러오는 데 실패했습니다. 다시 시도해 주세요.');
            if (dataTableInstance) {
                dataTableInstance.clear().draw();
            }
        })
        .finally(() => {
            if (loadingSpinner) {
                loadingSpinner.style.display = 'none';
            }
            $('#advancedSearchModal').modal('hide');
        });
}

// 이벤트 리스너를 설정하고 초기 데이터를 로드하는 함수
function setupUserTableEvents() {
    initializeUsersDomElements();

    if (simpleSearchInput && simpleSearchButton) {
        simpleSearchInput.addEventListener('keypress', function(event) {
            if (event.key === 'Enter' || event.keyCode === 13) {
                event.preventDefault();
                simpleSearchButton.click();
            }
        });

        simpleSearchButton.addEventListener('click', function() {
            const userId = simpleSearchInput.value.trim();
            const cond = {
                email: '',
                name: '',
                userId: userId,
                userIdList: []
            };
            loadTableData('POST', cond, 'Users');
        });
    }

    if (advancedSearchButton) {
        advancedSearchButton.addEventListener('click', function(e) {
            e.preventDefault();

            const userId = document.getElementById('search-userId').value.trim();
            const name = document.getElementById('search-name').value.trim();
            const email = document.getElementById('search-email').value.trim();
            const userIdListStr = document.getElementById('search-userIdList').value.trim();

            const userIdList = userIdListStr
                ? userIdListStr.split(',').map(item => item.trim()).filter(item => item !== '')
                : [];

            const cond = {
                email: email,
                name: name,
                userId: userId,
                userIdList: userIdList.length > 0 ? userIdList : []
            };
            loadTableData('POST', cond, 'Users');
        });
    }

    if (resetButton) {
        resetButton.addEventListener('click', function() {
            document.getElementById('advancedSearchForm').reset();
            const initialCond = {
                email: '',
                name: '',
                userId: '',
                userIdList: []
            };
            loadTableData('POST', initialCond, 'Users');
        });
    }

    // ⭐ 추가된 부분: 페이지 로드 시 초기 데이터를 고급 검색 파라미터로 로드합니다.
    const initialCond = {
        email: '',
        name: '',
        userId: '',
        userIdList: []
    };
    loadTableData('POST', initialCond, 'Users');
}