import { MemberType } from 'types/member';

export interface TeamApiType {
  teamId: string;
  teamInfo: TeamCustomHookType;
  accessToken: string | null;
  teamsStatus?: TeamsStatusType;
}

export interface TeamSubmitType {
  watchers: MemberType[];
  participants: MemberType[];
}

export interface TeamCustomHookType {
  title: string;
  place: string;
  startAt: string;
  interviewerNumber: number;
  watcherIds: string[];
  participantIds: string[];
}

export interface Team {
  title: string;
  place: string;
  startAt: string;
}

export interface InterviewTeamType {
  id: string;
  title: string;
  place: string;
  startAt: string;
  teamImage: string;
  status: TeamStatusType;
  participants: Pick<ParticipantType, 'memberId' | 'profileUrl'>[];
}

export interface InterviewTeamDetailType {
  id: string;
  title: string;
  place: string;
  startAt: string;
  teamImage: string;
  hostId: string;
  status: TeamStatusType;
  isParticipant: Boolean;
  interviewerNumber: number;
  interviewers: Array<number | null>;
  interviewees: Array<number | null>;
  watchers: WatcherType[];
  participants: ParticipantType[];
}

export interface ParticipantType {
  memberId: string;
  levellogId: string;
  preQuestionId: string;
  nickname: string;
  profileUrl: string;
}

export interface WatcherType {
  memberId: string;
  levellogId: string;
  preQuestionId: string;
  nickname: string;
  profileUrl: string;
}

export type TeamStatusType = 'READY' | 'IN_PROGRESS' | 'CLOSED' | '';
export type TeamsStatusType = 'open' | 'close';
