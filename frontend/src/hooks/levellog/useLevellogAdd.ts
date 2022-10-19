import { useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { Editor } from '@toast-ui/react-editor';

import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE } from 'constants/constants';

import { LevellogPostRequestType, requestPostLevellog } from 'apis/levellog';
import { teamGetUriBuilder } from 'utils/uri';

const useLevellogAdd = () => {
  const { showSnackbar } = useSnackbar();
  const levellogRef = useRef<Editor>(null);
  const navigate = useNavigate();
  const { teamId } = useParams();

  const accessToken = localStorage.getItem('accessToken');

  const { mutate: postLevellog } = useMutation(
    ({ teamId, levellog }: Omit<LevellogPostRequestType, 'accessToken'>) => {
      return requestPostLevellog({ accessToken, teamId, levellog });
    },
    {
      onSuccess: () => {
        showSnackbar({ message: MESSAGE.LEVELLOG_ADD });
        navigate(teamGetUriBuilder({ teamId }));
      },
    },
  );

  const handleClickLevellogAddButton = () => {
    if (typeof teamId !== 'string') {
      showSnackbar({ message: MESSAGE.WRONG_ACCESS });
      return;
    }

    if (!levellogRef.current) return;
    postLevellog({
      teamId,
      levellog: { content: levellogRef.current.getInstance().getMarkdown() },
    });
  };

  return {
    levellogRef,
    handleClickLevellogAddButton,
  };
};

export default useLevellogAdd;
