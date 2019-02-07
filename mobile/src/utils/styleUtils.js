import { Dimensions } from 'react-native';
import commonStyles from '../styles/commonStyles';

export default function getContainerStyle() {
  const { container, containerHighResolutions } = commonStyles;
  const screen = Dimensions.get('screen');
  return screen.height > 800 && screen.width > 800
    ? [container, containerHighResolutions] :
    container;
}
