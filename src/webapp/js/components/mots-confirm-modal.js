import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Modal from 'react-modal';

class MotsConfirmModal extends Component {
  constructor(props) {
    super(props);

    this.getModalParent = this.getModalParent.bind(this);
  }

  getModalParent() {
    return document.querySelector(`#${this.props.modalParentId}`);
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
              className="btn btn-danger margin-left-sm margin-bottom-md"
              onClick={this.props.onHide}
            >
              Cancel
            </button>
          </div>
        </Modal>
      </div>
    );
  }
}

export default MotsConfirmModal;

MotsConfirmModal.propTypes = {
  showModal: PropTypes.bool,
  onHide: PropTypes.func.isRequired,
  onConfirm: PropTypes.func.isRequired,
  modalParentId: PropTypes.string.isRequired,
  modalText: PropTypes.string,
};

MotsConfirmModal.defaultProps = {
  showModal: false,
  modalText: '',
};
