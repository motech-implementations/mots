import { ActionConst } from 'react-native-router-flux';

const initialState = {
  currentScene: null,
};

export default function (state = initialState, { type, scene }) {
  switch (type) {
    case ActionConst.FOCUS:
      if (scene.key !== '0_modal') {
        return { ...state, currentScene: scene.key };
      }
      return { ...state };
    default:
      return state;
  }
}
