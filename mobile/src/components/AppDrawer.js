import React, { Component } from 'react';
import Drawer from 'react-native-drawer';
import { Actions, DefaultRenderer } from 'react-native-router-flux';
import PropTypes from 'prop-types';

import Menu from './Menu';

const drawerStyles = {
  drawer: {
    shadowColor: '#000',
    shadowOpacity: 0.3,
    shadowRadius: 1,
  },
  main: {
    paddingLeft: 0,
  },
};

class AppDrawer extends Component {
  state = {};

  render() {
    const state = this.props.navigationState;
    const { ...children } = state.children;
    return (
      <Drawer
        open={state.open}
        onOpen={() => Actions.refresh({ key: state.key, open: true })}
        onClose={() => Actions.refresh({ key: 'drawer', open: false })}
        type="displace"
        content={<Menu />}
        tapToClose
        openDrawerOffset={0.2}
        panCloseMask={0.2}
        negotiatePan
        styles={drawerStyles}
        tweenHandler={ratio => ({
            mainOverlay: { opacity: ratio === 0 ? 0 : 0.3, backgroundColor: '#000' },
          })}
      >
        <DefaultRenderer
          navigationState={children[0]}
          onNavigate={this.props.onNavigate}
        />
      </Drawer>
    );
  }
}

export default AppDrawer;

AppDrawer.propTypes = {
  navigationState: PropTypes.shape({}).isRequired,
  onNavigate: PropTypes.func.isRequired,
};
