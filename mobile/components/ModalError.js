import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ModalInfo from './ModalInfo';

class ModalError extends Component {
  constructor(props) {
    super(props);

    this.state = {
      titleColor: '#ff0000',
      closeColor: '#ff0000',
    };
  }

  render() {
    return (
      <ModalInfo
        title={this.props.title}
        titleColor={this.state.titleColor}
        message={this.props.message}
        closeColor={this.state.closeColor}
        sceneKey="modalError"
      />
    );
  }
}

export default ModalError;

ModalError.propTypes = {
  title: PropTypes.string,
  message: PropTypes.string.isRequired,
};

ModalError.defaultProps = {
  title: '',
};
