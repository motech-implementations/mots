import { Platform, NetInfo } from 'react-native';
import { setConnectionState } from '../actions';
import { dispatch } from '../App';

const isIos = Platform.OS === 'ios';

export function setConnection(connInfo) {
    dispatch(setConnectionState(connInfo));
}

export async function retryIfNotConnected(connInfo) {
  if (connInfo.type === 'none' || connInfo.type === 'unknown') {
    return NetInfo.getConnectionInfo();
  }
  return connInfo;
}

export function handleConnectivityChange(connInfo) {
  if (isIos) {
    // retry the connection check on ios
    // https://github.com/facebook/react-native/issues/8615
    retryIfNotConnected(connInfo).then((connInfo) => 
    setConnection(connInfo));
  } else {
    setConnection(connInfo);
  }
}
