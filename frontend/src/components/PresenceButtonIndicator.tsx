const isButtonShow = {
  levellogLook: ({ interviewerId, loginUserId, levellogId }: IsButtonShowProps) => {
    if (interviewerId === loginUserId && levellogId) return true;
    if (interviewerId !== loginUserId) return true;

    return false;
  },
  levellogWrite: ({ interviewerId, loginUserId, levellogId }: IsButtonShowProps) => {
    if (interviewerId === loginUserId && !levellogId) return true;

    return false;
  },
  interviewQuestionLook: ({ interviewerId, loginUserId }: IsButtonShowProps) => {
    if (interviewerId === loginUserId) return true;

    return false;
  },
  preQuestionLook: ({ interviewerId, loginUserId, preQuestionId }: IsButtonShowProps) => {
    if (interviewerId === loginUserId) return false;
    if (preQuestionId) return true;

    return false;
  },
  preQuestionWrite: ({ interviewerId, loginUserId, preQuestionId }: IsButtonShowProps) => {
    if (interviewerId === loginUserId) return false;
    if (!preQuestionId) return true;

    return false;
  },
};

const PresenceButtonIndicator = ({ type, children, ...props }: PresenceButtonIndicatorProps) => {
  return <>{isButtonShow[type]({ ...props }) && children}</>;
};

export default PresenceButtonIndicator;

interface PresenceButtonIndicatorProps extends IsButtonShowProps {
  type:
    | 'levellogLook'
    | 'levellogWrite'
    | 'interviewQuestionLook'
    | 'preQuestionLook'
    | 'preQuestionWrite';
  children: JSX.Element;
}

interface IsButtonShowProps {
  interviewerId: string;
  loginUserId: string;
  levellogId?: string;
  preQuestionId?: string;
}