import styled from 'styled-components';

import useTeams from 'hooks/useTeams';

import EmptyTeams from 'pages/status/EmptyTeams';

import { useQuery } from '@tanstack/react-query';
import InterviewTeam from 'components/teams/InterviewTeam';
import { InterviewTeamType, TeamConditionsType } from 'types/team';

const Teams = ({ teamsCondition }: TeamsProps) => {
  const { getTeams } = useTeams();

  const getTeamsQuery = useQuery(['teams', teamsCondition], () => getTeams({ teamsCondition }), {
    suspense: true,
  });

  return (
    <S.Container>
      {getTeamsQuery.data?.teams.map((team: InterviewTeamType) => (
        <InterviewTeam key={team.id} team={team} />
      ))}
      {getTeamsQuery.data?.teams.length === 0 && <EmptyTeams />}
    </S.Container>
  );
};

interface TeamsProps {
  teamsCondition: TeamConditionsType;
}

const S = {
  Container: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 3.125rem;
    @media (max-width: 560px) {
      justify-content: center;
      gap: 2.5rem;
    }
  `,
};

export default Teams;