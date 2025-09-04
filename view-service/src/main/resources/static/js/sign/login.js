// Handles the actual data submission for signin
const handleSignin = async (form) => {
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());
    const endpoint = 'http://localhost:8000/user-service/login';
    const rememberMeCheckbox = document.getElementById('customCheck');

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            // 응답 헤더에서 토큰과 userId를 읽어옴
            const authToken = response.headers.get('auth-token');
            const userId = response.headers.get('user-id');

            // 토큰과 userId를 sessionStorage에 저장
            if (authToken && userId) {
                console.log('로그인 성공:', { authToken, userId });
                sessionStorage.setItem('auth-token', authToken);
                sessionStorage.setItem('user-id', userId);

                // 'Remember Me' 체크박스가 체크되어 있으면 이메일을 localStorage에 저장
                if (rememberMeCheckbox.checked) {
                    localStorage.setItem('rememberedEmail', data.email);
                } else {
                    // 체크가 해제되어 있으면 저장된 이메일 삭제
                    localStorage.removeItem('rememberedEmail');
                }

                // 로그인 성공 시 home 페이지로 리디렉션
                window.location.href = '/home'; // /home 경로로 이동
            } else {
                console.error('응답 헤더에 토큰 또는 userId가 없습니다.');
                alert('로그인에 실패했습니다.');
            }
        } else {
            // If login fails, display the error message from the server
            const error = await response.json();
            console.error('로그인 실패:', error);
            alert('회원정보가 잘못되었습니다.');
        }
    } catch (error) {
        console.error('네트워크 오류:', error);
        alert('네트워크 오류가 발생했습니다. 다시 시도해 주세요.');
    }
};

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    const emailInput = document.getElementById('email');
    const rememberMeCheckbox = document.getElementById('customCheck'); // 체크박스 엘리먼트 가져오기

    // 페이지 로드 시 localStorage에 저장된 이메일이 있는지 확인하고 있으면 입력
    const rememberedEmail = localStorage.getItem('rememberedEmail');
        if (rememberedEmail) {
            emailInput.value = rememberedEmail;
            rememberMeCheckbox.checked = true; // 체크박스를 체크 상태로 변경
        }

    // form의 제출 이벤트를 감지
    loginForm.addEventListener('submit', (event) => {
        // 페이지가 새로고침되는 기본 동작을 막음
        event.preventDefault(); 
        
        // handleSignin 함수 호출
        handleSignin(loginForm);
    });
});
