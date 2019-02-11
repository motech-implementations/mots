import { getStatusBarHeight } from 'react-native-status-bar-height';

const statusBarHeight = getStatusBarHeight(true);

export default {
  buttonContainer: {
    marginBottom: 20,
  },
  header: {
    fontSize: 16,
    color: 'black',
    textAlignVertical: 'center',
  },
  headerContainer: {
    paddingTop: statusBarHeight,
    height: 50 + statusBarHeight,
    flexDirection: 'row',
    justifyContent: 'center',
    alignContent: 'center',
    backgroundColor: '#B4B7C0',
  },
  iconContainer: {
    position: 'absolute',
    borderRadius: 3,
    paddingVertical: 5,
    paddingHorizontal: 10,
    backgroundColor: '#B4B7C0',
  },
  icon: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  selectContainer: {
    marginLeft: 35,
    marginRight: 0,
  },
};
