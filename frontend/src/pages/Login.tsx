import { useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';

import axios, { AxiosResponse } from 'axios';

import useSnackbar from 'hooks/useSnackbar';
import useUser from 'hooks/useUser';

import { ROUTES_PATH } from 'constants/constants';

import { requestGetUserAuthority, requestGetUserLogin } from 'apis/login';
import { 토큰이올바르지못한경우홈페이지로 } from 'apis/utils';

const Login = () => {
  const { userInfoDispatch } = useUser();
  const { showSnackbar } = useSnackbar();
  const location = useLocation() as unknown as { state: { pathname: string } };
  const navigate = useNavigate();

  useEffect(() => {
    const loginGithub = async () => {
      const params = new URL(window.location.href).searchParams;
      const code = params.get('code');
      const accessToken = localStorage.getItem('accessToken');
      try {
        if (accessToken) {
          const res = await requestGetUserAuthority({ accessToken });
          userInfoDispatch({
            id: res.data.id,
            nickname: res.data.nickname,
            profileUrl: res.data.profileUrl,
          });
        }

        if (code) {
          const res = await requestGetUserLogin({ code });
          localStorage.setItem('accessToken', res.data.accessToken);
          const accessToken = localStorage.getItem('accessToken')!;
          const resLogin = await requestGetUserAuthority({ accessToken });
          userInfoDispatch({
            id: resLogin.data.id,
            nickname: resLogin.data.nickname,
            profileUrl: resLogin.data.profileUrl,
          });
        }

        if (location.state && location.state.pathname !== ROUTES_PATH.LOGIN) {
          navigate(location.state.pathname, { replace: true });

          return;
        }

        navigate(ROUTES_PATH.HOME, { replace: true });
      } catch (err: unknown) {
        if (axios.isAxiosError(err) && err instanceof Error) {
          const responseBody: AxiosResponse = err.response!;
          if (
            토큰이올바르지못한경우홈페이지로({ message: responseBody.data.message, showSnackbar })
          ) {
            showSnackbar({ message: responseBody.data.message });
          }
        }
      }
    };

    loginGithub();
  }, []);

  return <Outlet />;
};

export default Login;
