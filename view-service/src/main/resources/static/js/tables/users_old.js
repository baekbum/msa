document.addEventListener('DOMContentLoaded', function() {

    const loadingSpinner = document.getElementById('loading-spinner');
    let dataTableInstance = null; // DataTables 인스턴스를 관리

    /**
     * @param {string} method - 'GET' 또는 'POST'
     * @param {object|string|null} payload - GET 요청 시 userId, POST 요청 시 검색 조건 객체
     * @param {string} title - 테이블 제목으로 표시될 텍스트
     */
    function loadTableData(method, payload, title) {
        document.getElementById('table-title').textContent = title;

        loadingSpinner.style.display = 'block';

        if (dataTableInstance) {
            dataTableInstance.clear();
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

                if (!dataTableInstance) {
                    dataTableInstance = $('#dataTable').DataTable();
                }

                if (users && users.length > 0) {
                    const tableRows = users.map((user, index) => {
                        const date = new Date(user.createdAt);
                        const formattedDate = date.toLocaleDateString('ko-KR', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit'
                        }).replace(/\. /g, '.');

                        return [
                            index + 1,
                            user.userId,
                            user.name,
                            user.email,
                            formattedDate
                        ];
                    });

                    dataTableInstance.rows.add(tableRows).draw();

                } else {
                    // 데이터가 없을 때 테이블 본문을 비웁니다.
                    const tableBody = document.querySelector('#dataTable tbody');
                    if (tableBody) {
                        tableBody.innerHTML = '';
                    }
                }
            })
            .catch(error => {
                console.error('테이블 데이터를 불러오는 데 실패했습니다:', error);
                // 네트워크 오류 발생 시 alert으로 메시지 표시
                alert('데이터를 불러오는 데 실패했습니다. 다시 시도해 주세요.');

                // ⭐ 수정된 부분:
                // DataTable 인스턴스가 존재하면 데이터만 비웁니다.
                // 이렇게 하면 테이블의 DOM 구조는 유지됩니다.
                if (dataTableInstance) {
                    dataTableInstance.clear().draw();
                }
            })
            .finally(() => {
                loadingSpinner.style.display = 'none';
                $('#advancedSearchModal').modal('hide');
            });
    }

    // 일반 검색 버튼 (USER ID 검색)
    const simpleSearchInput = document.querySelector('.navbar-search input[type="text"]');
    const simpleSearchButton = document.querySelector('.navbar-search .btn.btn-primary');

    // ⭐ 새로운 코드: input 박스에 Enter 키 이벤트 추가 ⭐
    simpleSearchInput.addEventListener('keypress', function(event) {
        // 엔터 키 (keyCode 13 또는 event.key 'Enter')가 눌렸는지 확인합니다.
        if (event.key === 'Enter' || event.keyCode === 13) {
            event.preventDefault(); // 폼 제출 기본 동작을 방지합니다.
            simpleSearchButton.click(); // 검색 버튼 클릭을 트리거합니다.
        }
    });

    simpleSearchButton.addEventListener('click', function() {
        const userId = simpleSearchInput.value.trim();
        if (userId) {
            loadTableData('GET', { userId: userId }, `User: ${userId}`);
        } else {
            loadTableData('POST', null, 'Users');
        }
    });

    // 고급 검색 버튼 (모달)
    document.getElementById('advanced-search-btn').addEventListener('click', function(e) {
        e.preventDefault();

        const userId = document.getElementById('search-userId').value;
        const name = document.getElementById('search-name').value;
        const email = document.getElementById('search-email').value;
        const userIdListStr = document.getElementById('search-userIdList').value;
        const userIdList = userIdListStr ? userIdListStr.split(',').map(item => item.trim()) : [];

        const cond = {
            id: null,
            email: email || null,
            name: name || null,
            userId: userId || null,
            userIdList: userIdList || null
        };

        loadTableData('POST', cond, 'Users (Advanced Search)');
    });

    // ⭐ 추가된 코드: 초기화 버튼 이벤트 리스너 ⭐
    document.getElementById('reset-btn').addEventListener('click', function() {
        // 'advancedSearchForm'의 모든 입력 필드를 초기화합니다.
        document.getElementById('advancedSearchForm').reset();
    });

    // 네비게이션 링크 (모든 유저 검색)
    document.querySelectorAll('.nav-link-ajax').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            loadTableData('POST', null, this.textContent);
        });
    });

    // 페이지 로드 시 초기 데이터 로드
    document.querySelector('.nav-link-ajax[data-target="users"]').click();
});