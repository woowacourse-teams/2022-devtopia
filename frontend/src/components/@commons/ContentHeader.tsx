import React from 'react';
import styled from 'styled-components';

const ContentHeader = ({ title, children }: InputProps) => {
  return (
    <ContentHeaderStyle>
      <h1>{title}</h1>
      <ButtonBox>{children}</ButtonBox>
    </ContentHeaderStyle>
  );
};

interface InputProps {
  title: string;
  children?: React.ReactNode;
}

const ButtonBox = styled.div`
  position: absolute;
  right: 0;
`;

const ContentHeaderStyle = styled.div`
  height: 120px;
  width: 100%;
  display: flex;
  position: relative;
  justify-content: center;
  align-items: center;
`;

export default ContentHeader;
