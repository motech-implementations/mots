import React, { Component } from 'react';
import { Platform, View, KeyboardAvoidingView, ScrollView, Text, Keyboard, Image, Dimensions } from 'react-native';
import { Actions, ActionConst } from 'react-native-router-flux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import InputWithIcon from '../components/InputWithIcon';
import { signIn, signInOffline } from '../actions';
import Button from '../components/Button';
import Spinner from '../components/Spinner';
import styles from '../styles/formsStyles';
import commonStyles from '../styles/commonStyles';
import Footer from '../components/Footer';

const { lightThemeText } = commonStyles;
const image = require('../img/EBODAClogo.jpg');
const isIos = Platform.OS === 'ios';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      error: '',
      loading: false,
      dim: Dimensions.get('window'),
    };

    this.onButtonPress = this.onButtonPress.bind(this);
    this.dimensionHandler = this.dimensionHandler.bind(this);
  }

  componentWillMount() {
    Dimensions.addEventListener('change', this.dimensionHandler);
  }

  componentWillUnmount() {
    Dimensions.removeEventListener('change', this.dimensionHandler);
  }

  onButtonPress() {
    this.setState({ error: '', loading: true });
    const { username, password } = this.state;
    const savedLogin = this.props.savedLogins[username];
    const signInFn = (this.props.isConnected) ? this.props.signIn : this.props.signInOffline;
    signInFn(
      username,
      password,
      savedLogin,
      () => this.onLoginSuccess(),
      () => this.onLoginFail(),
    );
  }

  onLoginSuccess() {
    this.setState({
      username: '', password: '', error: '', loading: false,
    });
    Keyboard.dismiss();
    Actions.drawer({ type: ActionConst.RESET });
  }

  onLoginFail() {
    this.setState({ loading: false, error: this.props.error });
  }

  dimensionHandler() {
    this.setState({
      dim: Dimensions.get('window'),
    });
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
      <View style={{ flex: 1, alignItems: 'center' }}>
        { this.state.dim.height > 320 &&
          <Image
            resizeMode="contain"
            style={{
                width: this.state.dim.width > 400 ? 400 : this.state.dim.width,
                // height = width / 3.5 <= 3.5 is approx image ratio
                height: parseInt((
                    this.state.dim.width > 400 ? 300 : this.state.dim.width) / 3.5, 10),
                paddingHorizontal: 10,
              }}
            source={image}
          />
          }

        <ScrollView
          style={[
            styles.scrollableCard,
            {
              width: this.state.dim.width - 10,
              marginTop: this.state.dim.height > 320 ? 0 : 10,
            },
          ]}
          alwaysBounceVertical={false}
        >
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

          <View style={[styles.cardRow]}>
            {this.renderButton()}
          </View>

          <Footer />
        </ScrollView>
      </View>
    );
  }
}

function mapStateToProps(state) {
  return {
    savedLogins: state.auth.savedLogins,
    isConnected: state.connectionReducer.isConnected,
    error: state.auth.error,
  };
}

export default connect(mapStateToProps, { signIn, signInOffline })(Login);

Login.propTypes = {
  signIn: PropTypes.func.isRequired,
  signInOffline: PropTypes.func.isRequired,
  isConnected: PropTypes.bool.isRequired,
  error: PropTypes.string,
  savedLogins: PropTypes.shape({}).isRequired,
};

Login.defaultProps = {
  error: '',
};
