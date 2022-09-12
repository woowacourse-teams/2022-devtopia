import { Link } from 'react-router-dom';

import styled from 'styled-components';

import useUtil from 'hooks/useUtil';

import checkIcon from 'assets/images/check.svg';
import locationIcon from 'assets/images/location.svg';
import { GITHUB_AVATAR_SIZE_LIST } from 'constants/constants';

import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';
import { InterviewTeamType, ParticipantType } from 'types/team';
import { convertDateAndTime, createParam } from 'utils/util';

const InterviewTeam = ({ team }: InterviewTeamsProp) => {
  const { id, teamImage, title, status, place, startAt, participants } = team;

  return (
    <Link to={createParam({ teams: id })} state={team}>
      <S.Container status={status}>
        <FlexBox gap={0.625}>
          <Image
            src={teamImage}
            sizes={'LARGE'}
            boxShadow={true}
            githubAvatarSize={GITHUB_AVATAR_SIZE_LIST.LARGE}
          />
          <FlexBox flexFlow="column wrap" gap={0.625}>
            <S.Title>{title}</S.Title>
          </FlexBox>
        </FlexBox>
        <FlexBox flexFlow="column">
          <S.Info>
            <S.Notice>
              <S.ImageBox>
                <Image src={locationIcon} sizes={'TINY'} />
              </S.ImageBox>
              {place}
            </S.Notice>
          </S.Info>
          <S.Info>
            <S.Notice>
              <S.ImageBox>
                <Image src={checkIcon} sizes={'TINY'} />
              </S.ImageBox>
              {convertDateAndTime({ startAt })}
            </S.Notice>
          </S.Info>
        </FlexBox>
        <S.ParticipantsBox>
          {participants.map((participant: ParticipantType) => (
            <Image
              key={participant.memberId}
              src={participant.profileUrl}
              sizes={'SMALL'}
              githubAvatarSize={GITHUB_AVATAR_SIZE_LIST.SMALL}
            />
          ))}
        </S.ParticipantsBox>
      </S.Container>
    </Link>
  );
};

interface InterviewTeamsProp {
  team: InterviewTeamType;
}

const S = {
  Container: styled.div<{ status: string }>`
    display: flex;
    flex-direction: column;
    gap: 1.25rem;
    width: 22.6563rem;
    height: 15rem;
    padding: 1.25rem 1.5rem 1.875rem 1.5rem;
    border-radius: 0.625rem;
    box-shadow: 0.0625rem 0.25rem 0.625rem ${(props) => props.theme.new_default.GRAY};
    opacity: ${(props) => (props.status === 'CLOSED' ? 0.2 : 1)};
    cursor: pointer;
  `,

  Title: styled.h3`
    width: 11.5rem;
    margin-top: 0.3125rem;
    line-height: 1.5625rem;
    word-break: break-all;
  `,

  Info: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 100%;
    height: 1.4375rem;
    margin-bottom: 0.375rem;
    font-size: 0.875rem;
    font-weight: 600;
  `,

  Notice: styled.div`
    display: flex;
    align-items: center;
    color: ${(props) => props.theme.default.DARK_GRAY};
  `,

  ImageBox: styled.div`
    margin-right: 0.75rem;
  `,

  ParticipantsBox: styled.div`
    display: flex;
    flex-direction: row;
    width: 100%;
    gap: 0.25rem;
    overflow-x: auto;
    // chrome, Safari, Opera
    ::-webkit-scrollbar {
      display: none;
    }
    // Edge
    -ms-overflow-style: none;
    // Firefox
    scrollbar-width: none;
  `,
};

export default InterviewTeam;
