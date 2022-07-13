import React from 'react';
import styled from 'styled-components';
import { BASE_INPUTSIZE } from '../../constants/constants';

const Input = ({
  width = BASE_INPUTSIZE.WIDTH,
  height = BASE_INPUTSIZE.HEIGHT,
  inputRef,
  ...rest
}: InputProps) => {
  return <InputStyle width={width} height={height} ref={inputRef} {...rest} />;
};

interface InputProps {
  height?: string;
  width?: string;
  inputRef?: React.Ref<HTMLInputElement>;
}

export const InputStyle = styled.input`
  font-size: 1.2rem;
  background-color: #f1f1f1;
  border-style: none;
  border-radius: 5px;
  box-sizing: border-box;
  width: ${(props) => props.width};
  height: ${(props) => props.height};
`;

export default Input;
