import { Suspense } from 'react';
import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useTeamsCondition from 'hooks/team/useTeamsCondition';
import useUser from 'hooks/useUser';

import Loading from 'pages/status/Loading';

import plusIcon from 'assets/images/plus.svg';
import { ROUTES_PATH } from 'constants/constants';

import Teams from '../../components/teams/Teams';
import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FilterButton from 'components/@commons/FilterButton';
import Image from 'components/@commons/Image';

const InterviewTeams = () => {
  const {
    teamsCondition,
    handleClickCloseTeamsButton,
    handleClickMyTeamsButton,
    handleClickOpenTeamsButton,
  } = useTeamsCondition();
  const { loginUserId } = useUser();

  return (
    <>
      <ContentHeader title={'인터뷰 팀'}>
        <S.FilterUl>
          <li>
            <FilterButton
              onClick={handleClickOpenTeamsButton}
              isActive={teamsCondition.open}
              aria-label={'진행중인 인터뷰 팀으로 필터링'}
              aria-disabled={teamsCondition.open}
            >
              진행중인 인터뷰
            </FilterButton>
          </li>
          <li>
            <FilterButton
              onClick={handleClickCloseTeamsButton}
              isActive={teamsCondition.close}
              aria-label={'종료된 인터뷰 팀으로 필터링'}
              aria-disabled={teamsCondition.close}
            >
              종료된 인터뷰
            </FilterButton>
          </li>
          {loginUserId && (
            <li>
              <FilterButton
                onClick={handleClickMyTeamsButton}
                isActive={teamsCondition.my}
                aria-label={'나의 인터뷰 팀 목록으로 필터링'}
                aria-disabled={teamsCondition.my}
              >
                나의 인터뷰
              </FilterButton>
            </li>
          )}
        </S.FilterUl>
        <span />
      </ContentHeader>
      <S.Container>
        <Suspense fallback={<Loading />}>
          <Teams teamsCondition={teamsCondition} />
        </Suspense>
        <Link to={ROUTES_PATH.INTERVIEW_TEAMS_ADD}>
          <S.TeamAddButton>
            {'팀 추가하기'}
            <S.ImageBox>
              <Image src={plusIcon} sizes={'TINY'} />
            </S.ImageBox>
          </S.TeamAddButton>
        </Link>
      </S.Container>
    </>
  );
};

const S = {
  FilterUl: styled.ul`
    display: flex;
    align-items: center;
    gap: 0.625rem;
  `,

  Container: styled.main`
    overflow: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    @media (min-width: 1620px) {
      padding: 0 calc((100vw - 100rem) / 2);
      padding-bottom: 6.25rem;
    }
    @media (min-width: 1187.5px) and (max-width: 1620px) {
      padding: 0 calc((100vw - 74.2188rem) / 2);
      padding-bottom: 6.25rem;
    }
    @media (min-width: 775px) and (max-width: 1207.5px) {
      padding: 0 calc((100vw - 48.4375rem) / 2);
      padding-bottom: 6.25rem;
    }
    @media (min-width: 560px) and (max-width: 800px) {
      padding: 0 calc((100vw - 22.6563rem) / 2);
      padding-bottom: 6.25rem;
    }
    @media (max-width: 560px) {
      padding: 0 1.25rem;
      padding-bottom: 6.25rem;
    }
  `,

  Empty: styled.div`
    display: flex;
    flex-direction: column;
    gap: 3.125rem;
    @media (max-width: 560px) {
      justify-content: center;
      gap: 2.5rem;
    }
  `,

  Content: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 3.125rem;
    @media (max-width: 560px) {
      justify-content: center;
      gap: 2.5rem;
    }
  `,

  TeamAddButton: styled(Button)`
    display: flex;
    justify-content: center;
    align-items: center;
    position: fixed;
    left: 0;
    right: 0;
    bottom: 6.875rem;
    z-index: 10;
    width: 8.125rem;
    height: 3.125rem;
    margin: 0 auto;
    border-radius: 2rem;
    background-color: ${(props) => props.theme.new_default.DARK_BLACK};
    font-size: 1rem;
  `,

  ImageBox: styled.div`
    margin: 0 0 0.0625rem 0.3125rem;
  `,
};

export default InterviewTeams;
