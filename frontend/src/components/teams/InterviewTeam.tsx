import styled from 'styled-components';

import check from 'assets/images/check.png';
import placeHolder from 'assets/images/placeholder.png';

import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';
import { InterviewTeamType, ParticipantType } from 'types/team';

const InterviewTeam = ({
  id,
  teamImage,
  hostId,
  title,
  place,
  startAt,
  status,
  participants,
}: InterviewTeamType) => {
  return (
    <S.Container id={id} status={status}>
      <FlexBox gap={0.625}>
        <Image src={teamImage} sizes={'LARGE'} boxShadow={true} />
        <FlexBox flexFlow="column wrap" gap={0.625}>
          <S.Title id={id}>{title}</S.Title>
        </FlexBox>
      </FlexBox>
      <FlexBox flexFlow="column">
        <S.Info>
          <S.Notice>
            <S.ImageBox>
              <Image src={placeHolder} sizes={'TINY'} />
            </S.ImageBox>
            {place}
          </S.Notice>
        </S.Info>
        <S.Info>
          <S.Notice>
            <S.ImageBox>
              <Image src={check} sizes={'TINY'} />
            </S.ImageBox>
            {`${startAt.slice(0, 4)}년 ${startAt.slice(5, 7)}월 ${startAt.slice(
              8,
              10,
            )}일 ${startAt.slice(11, 13)}시`}
          </S.Notice>
        </S.Info>
      </FlexBox>
      <S.ParticipantsBox>
        {participants.map((participant: ParticipantType) => (
          <Image key={participant.memberId} src={participant.profileUrl} sizes={'SMALL'} />
        ))}
      </S.ParticipantsBox>
    </S.Container>
  );
};

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

  Notice: styled.p`
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
