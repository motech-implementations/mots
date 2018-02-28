import React, { Component } from 'react';
import { View, Text, ScrollView } from 'react-native';
import { Actions } from 'react-native-router-flux';
import PropTypes from 'prop-types';
import Button from './Button';
import modalInfoSyles from '../styles/modalInfoStyles';

const {
  modalContainer, modalBox, title, message, buttonsRow,
} = modalInfoSyles;

export default class ModalInfo extends Component {
  componentWillUnmount() {
    if (this.props.onClose) {
      this.props.onClose();
    }
  }

  render() {
    return (
      <View style={modalContainer}>
        <View style={modalBox}>
          { !!this.props.title &&
          <Text style={[title, { color: this.props.titleColor }]}>
            {this.props.title}
          </Text>
          }
          <ScrollView style={{ maxHeight: 250 }} >
            <Text style={message}>{this.props.message}</Text>
          </ScrollView>
          <View style={buttonsRow}>
            { this.props.onConfirm &&
            <View style={{ marginRight: 5 }}>
              <Button
                onPress={() => { this.props.onConfirm(); Actions.pop(); }}
                iconName="check"
                iconColor="#FFF"
                buttonColor={this.props.confirmColor}
                style={{ width: 50 }}
              >
                {this.props.confirmButtonText}
              </Button>
            </View>
          }
            <Button
              onPress={() => {
                Actions.pop({ key: 'modalInfo' });
              }}
              iconName="times-circle"
              iconColor="#FFF"
              buttonColor={this.props.closeColor}
              style={{ width: 50 }}
            >
              {this.props.closeButtonText}
            </Button>
          </View>
        </View>
      </View>
    );
  }
}

ModalInfo.propTypes = {
  title: PropTypes.string,
  titleColor: PropTypes.string,
  message: PropTypes.string.isRequired,
  confirmColor: PropTypes.string,
  closeColor: PropTypes.string,
  onConfirm: PropTypes.func,
  onClose: PropTypes.func,
  confirmButtonText: PropTypes.string,
  closeButtonText: PropTypes.string,
};

ModalInfo.defaultProps = {
  title: '',
  titleColor: '#337ab7',
  confirmColor: '#337ab7',
  closeColor: '#337ab7',
  onConfirm: null,
  onClose: null,
  confirmButtonText: 'Confirm',
  closeButtonText: 'Close',
};
