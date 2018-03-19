import React, { Component } from 'react';
import { View, Text, Keyboard } from 'react-native';
import { Actions, ActionConst } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import InputWithIcon from '../components/InputWithIcon';
import { signinUser } from '../actions';
import Button from '../components/Button';
import Spinner from '../components/Spinner';
import styles from '../styles/formsStyles';
import commonStyles from '../styles/commonStyles';

const { lightThemeText } = commonStyles;

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '', password: '', error: '', loading: false,
    };

    this.onButtonPress = this.onButtonPress.bind(this);
  }

  onButtonPress() {
    this.setState({ error: '', loading: true });
    const { username, password } = this.state;
    this.props.signinUser({
      username,
      password,
    }, (() => this.onLoginSuccess()), () => this.onLoginFail());
  }

  onLoginSuccess() {
    this.setState({
      username: '', password: '', error: '', loading: false,
    });
    Keyboard.dismiss();
    Actions.drawer({ type: ActionConst.RESET });
  }

  onLoginFail() {
    this.setState({ error: 'Wrong username or password. Please try again.', loading: false });
  }

  renderButton() {
    if (this.state.loading) {
      return <Spinner size="small" />;
    }

    return (
      <Button
        onPress={this.onButtonPress}
        iconName="sign-in"
        iconColor="#FFF"
        buttonColor="#449C44"
      >
        Log in
      </Button>
    );
  }

  render() {
    return (
      <View style={[styles.mainCard, { marginTop: 80 }]}>
        <View style={styles.headerRow}>
          <Text style={lightThemeText}>MOTS Login</Text>
        </View>

        <View style={styles.cardRow}>
          <InputWithIcon
            iconName="user"
            iconColor="#555"
            iconSize={20}
            placeholder="username"
            label="Username"
            value={this.state.username}
            onChangeText={username => this.setState({ username })}
          />
        </View>

        <View style={styles.cardRow}>
          <InputWithIcon
            iconName="lock"
            iconColor="#555"
            iconSize={20}
            secureTextEntry
            placeholder="password"
            label="Password"
            value={this.state.password}
            onChangeText={password => this.setState({ password })}
          />
        </View>

        <Text style={styles.errorTextStyle}>
          {this.state.error}
        </Text>

        <View style={styles.cardRow}>
          {this.renderButton()}
        </View>
      </View>
    );
  }
}

export default connect(null, { signinUser })(Login);

Login.propTypes = {
  signinUser: PropTypes.func.isRequired,
};
