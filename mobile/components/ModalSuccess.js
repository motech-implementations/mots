import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ModalInfo from './ModalInfo';

class ModalSuccess extends Component {
  constructor(props) {
    super(props);

    this.state = {
      title: 'Success!',
      titleColor: '#449C44',
      closeColor: '#449C44',
    };
  }

  render() {
    return (
      <ModalInfo
        title={this.state.title}
        titleColor={this.state.titleColor}
        message={this.props.message}
        closeColor={this.state.closeColor}
        onClose={this.props.onClose}
        sceneKey="modalError"
      />
    );
  }
}

export default ModalSuccess;

ModalSuccess.propTypes = {
  message: PropTypes.string.isRequired,
  onClose: PropTypes.func,
};

ModalSuccess.defaultProps = {
  onClose: null,
};
