import { fetcher } from 'apis';

import { InterviewQuestionSort } from 'types/interviewQuestion';
import { InterviewQuestionApiType, InterviewQuestionSearchApiType } from 'types/interviewQuestion';

export const requestSearchedInterviewQuestion = async ({
  accessToken,
  keyword,
  page = 0,
  size = 2000,
  sort = 'latest',
}: SearchedInterviewQuestionType): Promise<InterviewQuestionSearchApiType> => {
  const { data } = await fetcher.get(
    `/interview-questions?keyword=${keyword}&page=${page}&size=${size}&sort=${sort}`,
  );

  return data;
};

export const requestLikeInterviewQuestion = async ({
  accessToken,
  interviewQuestionId,
}: Pick<InterviewQuestionApiType, 'accessToken' | 'interviewQuestionId'>): Promise<void> => {
  const { data } = await fetcher.post(
    `/interview-questions/${interviewQuestionId}/like`,
    {}, // 지금은 이거 없애면 headers가 전달 안 돼서 에러 뜸
    {
      headers: { Authorization: `Bearer ${accessToken}` },
    },
  );

  return data;
};

export const requestLikeCancelInterviewQuestion = async ({
  accessToken,
  interviewQuestionId,
}: Pick<InterviewQuestionApiType, 'accessToken' | 'interviewQuestionId'>): Promise<void> => {
  const { data } = await fetcher.delete(`/interview-questions/${interviewQuestionId}/like`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  return data;
};

interface SearchedInterviewQuestionType {
  accessToken: string | null;
  keyword: string | number | null;
  page?: number;
  size?: number;
  sort: InterviewQuestionSort;
}
