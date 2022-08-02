import styled from 'styled-components';

const Button = ({
  children,
  type = 'button',
  disabled = false,
  onClick,
  color,
  ...props
}: ButtonProps) => {
  return (
    <ButtonStyle disabled={disabled} onClick={onClick} color={color} {...props}>
      <ButtonContent>{children}</ButtonContent>
    </ButtonStyle>
  );
};

interface ButtonProps {
  children: string;
  type?: string;
  disabled?: boolean;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  color?: string;
}

const ButtonStyle = styled.button`
  width: fit-content;
  padding: 0.625rem 1.125rem;
  border: none;
  border-radius: 0.375rem;
  background-color: ${(props) => props.theme.default.GRAY};
  font-size: 1rem;
  font-weight: 500;
`;

const ButtonContent = styled.p`
  white-space: pre;
`;

export default Button;
