import { ComponentMeta, ComponentStory } from '@storybook/react';
import BottomBar from 'components/@commons/bottomBar/BottomBar';

export default {
  title: 'BottomBar',
  component: BottomBar,
} as ComponentMeta<typeof BottomBar>;

const Template: ComponentStory<typeof BottomBar> = () => (
  <BottomBar buttonText={'제출하기'} handleClickRightButton={() => {}} />
);

export const Base = Template.bind({});