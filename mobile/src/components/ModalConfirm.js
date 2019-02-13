import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ModalInfo from './ModalInfo';

class ModalConfirm extends Component {
  constructor(props) {
    super(props);

    this.state = {
      closeColor: 'grey',
      closeButtonText: 'Cancel',
    };
  }

  render() {
    return (
      <ModalInfo
        message={this.props.message}
        closeColor={this.state.closeColor}
        closeButtonText={this.state.closeButtonText}
        onConfirm={this.props.onConfirm}
        supportedOrientations={['portrait', 'landscape']}
        sceneKey="modalConfirm"
      />
    );
  }
}

export default ModalConfirm;

ModalConfirm.propTypes = {
  message: PropTypes.string.isRequired,
  onConfirm: PropTypes.func,
};

ModalConfirm.defaultProps = {
  onConfirm: null,
};
