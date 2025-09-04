document.addEventListener('DOMContentLoaded', function() {
    // 세션 스토리지에서 인증 토큰과 사용자 ID를 가져옵니다.
    const authToken = sessionStorage.getItem('auth-token');
    const userId = sessionStorage.getItem('user-id');

    // 토큰 또는 사용자 ID가 없으면 로그인 페이지로 리디렉션합니다.
    if (!authToken || !userId) {
        alert('비정상적인 접근입니다.');
        window.location.href = '/sign/login';
    }

    // 인증 정보가 유효하면 사용자 ID를 화면에 표시합니다.
    else {
        const userSpan = document.querySelector('.dropdown-toggle .text-gray-600');

        // 엘리먼트가 존재하면 텍스트를 userId 값으로 교체합니다.
        if (userSpan) {
            userSpan.textContent = userId;
        }
    }
});