import { getStatusBarHeight } from 'react-native-status-bar-height';

const statusBarHeight = getStatusBarHeight(true);

export default {
  container: {
    flex: 1,
    marginTop: 40 + statusBarHeight,
    backgroundColor: '#FFF',
  },
  containerHighResolutions: {
    marginTop: 60 + statusBarHeight,
  },
  center: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  lightThemeText: {
    color: '#000000',
  },
};
