import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Modal from 'react-modal';
import { connect } from 'react-redux';

import { resetLogoutCounter } from '../actions/index';

class MotsConfirmModal extends Component {
  constructor(props) {
    super(props);

    this.getModalParent = this.getModalParent.bind(this);
    this.hideConfirmModal = this.hideConfirmModal.bind(this);
  }

  getModalParent() {
    return document.querySelector(`#${this.props.modalParentId}`);
  }

  hideConfirmModal() {
    this.props.onHide();
    this.props.resetLogoutCounter();
  }

  render() {
    return (
      <div>
        <Modal
          className={{
            base: 'mots-confirm-modal',
            afterOpen: 'mots-confirm-modal_after-open',
            beforeClose: 'mots-confirm-modal_before-close',
          }}
          isOpen={this.props.showModal}
          parentSelector={this.getModalParent}
          onRequestClose={this.props.onHide}
          contentLabel="Modal"
          ariaHideApp={false}
        >
          <h3>{this.props.modalText}</h3>
          <div className="buttons-container padding-top-sm">
            <button
              type="submit"
              className="btn btn-primary margin-bottom-md"
              onClick={this.props.onConfirm}
            >
              Confirm
            </button>
            <button
              type="button"
              className="btn btn-danger margin-left-sm margin-bottom-md"
              onClick={this.hideConfirmModal}
            >
              Cancel
            </button>
          </div>
        </Modal>
      </div>
    );
  }
}

export default connect(null, { resetLogoutCounter })(MotsConfirmModal);

MotsConfirmModal.propTypes = {
  showModal: PropTypes.bool,
  onHide: PropTypes.func.isRequired,
  onConfirm: PropTypes.func.isRequired,
  modalParentId: PropTypes.string.isRequired,
  modalText: PropTypes.string,
  resetLogoutCounter: PropTypes.func.isRequired,
};

MotsConfirmModal.defaultProps = {
  showModal: false,
  modalText: '',
};
