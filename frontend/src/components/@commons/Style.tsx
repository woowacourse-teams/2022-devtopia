import styled from 'styled-components';

export const FeedbackContainer = styled.div`
  overflow: auto;
  width: 48rem;
  @media (max-width: 520px) {
    max-width: 22.875rem;
  }
`;

export const LogoStyle = styled.img`
  width: 40px;
  height: 42px;
`;

export const FeedbackTitle = styled.h2`
  margin-bottom: 1.875rem;
  font-size: 1.875rem;
`;
