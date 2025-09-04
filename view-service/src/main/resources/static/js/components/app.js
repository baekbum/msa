// src/main/resources/static/js/components/app.js

document.addEventListener('DOMContentLoaded', function() {
    const contentsWrapper = document.getElementById('contents-wrapper');
    const loadingSpinner = document.getElementById('loading-spinner');

    function loadPage(pageName) {
        // ⭐ 수정된 부분: 페이지 로딩 시작과 동시에 스피너를 보여줍니다.
        // 이 코드는 이제 users.js가 로드되고 초기 데이터 로딩이 완료될 때까지 스피너를 유지합니다.
        if (loadingSpinner) {
            loadingSpinner.style.display = 'block';
        }

        if ($.fn.DataTable.isDataTable('#dataTable')) {
            $('#dataTable').DataTable().destroy();
        }

        fetch(`/tables/${pageName}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(html => {
                contentsWrapper.innerHTML = html;

                const cssElement = document.createElement('link');
                cssElement.rel = 'stylesheet';
                cssElement.href = `/css/tables/${pageName}.css`;
                document.head.appendChild(cssElement);

                const scriptElement = document.createElement('script');
                scriptElement.src = `/js/tables/${pageName}.js`;
                scriptElement.onload = () => {
                    if (pageName === 'users') {
                        initializeUsersTable();
                        setupUserTableEvents();
                    }
                    // ⭐ 수정된 부분: 스크립트 로드 성공 시에는 users.js의 loadTableData 함수가
                    // 데이터를 불러온 후 스피너를 숨기므로, 여기서는 스피너를 숨기지 않습니다.
                };
                scriptElement.onerror = () => {
                    console.error(`${pageName}.js 스크립트 로드 실패`);
                    // 스크립트 로드 실패 시에는 스피너를 숨깁니다.
                    if (loadingSpinner) {
                        loadingSpinner.style.display = 'none';
                    }
                };
                document.body.appendChild(scriptElement);
            })
            .catch(error => {
                console.error('페이지 로드 실패:', error);
                // 페이지 로드 실패 시에는 스피너를 숨깁니다.
                if (loadingSpinner) {
                    loadingSpinner.style.display = 'none';
                }
            });
    }

    // 네비게이션 링크 클릭 이벤트
    document.querySelectorAll('.collapse-inner a').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const page = this.getAttribute('data-page');
            loadPage(page);
        });
    });

    // 초기 페이지 로드
    loadPage('users');
});