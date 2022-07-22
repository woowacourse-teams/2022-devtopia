import { useNavigate } from 'react-router-dom';

import { FeedbackPostType } from 'types';

import { ROUTES_PATH } from 'constants/constants';

import { deleteFeedbacks, getFeedbacks, postFeedback } from 'apis/feedback';

const useFeedback = () => {
  const accessToken = localStorage.getItem('accessToken');
  const navigate = useNavigate();

  const feedbackAdd = async ({ feedbackResult, levellogId }: any) => {
    try {
      await postFeedback(accessToken, { feedbackResult, levellogId });
    } catch (err) {
      alert(err.response.data.message);
      navigate(ROUTES_PATH.HOME);
    }
  };

  const feedbackLookup = async (levellogId: string) => {
    try {
      const res = await getFeedbacks(accessToken, levellogId);
      const feedbacks = res.data;

      return feedbacks;
    } catch (err) {
      console.log(err);
    }
  };

  const feedbackDelete = async (levellogId: string, feedbackId: string) => {
    try {
      const res = await deleteFeedbacks(accessToken, levellogId, feedbackId);
      return res.data;
    } catch (err) {
      alert(err.response.data.message);
      navigate(ROUTES_PATH.HOME);
    }
  };

  return { feedbackAdd, feedbackLookup, feedbackDelete };
};

export default useFeedback;
