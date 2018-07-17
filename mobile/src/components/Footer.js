import React, { Component } from 'react';
import { Text, View, Linking, Keyboard, Dimensions} from 'react-native';


const styles = {
  footerInfo: {
    width: '100%',
    height: 100,
    justifyContent: 'space-between',
    alignItems: 'center',
    position: 'absolute',
    bottom: 0,
    left:0,
    right:0,
    flexDirection: 'row',
  },

  textStyle: {
    textAlign: 'center',
    fontSize: 10,
    backgroundColor: '#e7e7e7',
    bottom:0,
    marginBottom:-20,
  }
};


class Footer extends Component {

  constructor(props) {
    super(props);
    this.state = {
      showFooter:true,
    };
  }

  isPortrait() {
    const dim = Dimensions.get('screen');
    if(dim.height >= dim.width) return true;
    else return false;
  };

  componentWillMount () {
    this.keyboardDidShowListener = Keyboard.addListener('keyboardDidShow', this._keyboardDidShow.bind(this));
    this.keyboardDidHideListener = Keyboard.addListener('keyboardDidHide', this._keyboardDidHide.bind(this));
  }

  componentWillUnmount () {
    this.keyboardDidShowListener.remove();
    this.keyboardDidHideListener.remove();
  }

  _keyboardDidShow () {
    this.setState({showFooter:false});
  }

  _keyboardDidHide () {
    this.setState({showFooter:true});
  }


  render() {
    return (
        <View style={styles.footerInfo}>
          {this.state.showFooter === true && this.isPortrait() === true && <Text style={styles.textStyle}>This project has received funding from the Innovative Medicines
            Initiative 2 Joint Undertaking under grant agreement EBOVAC1
            (grant nr. 115854), EBOVAC2 (grant nr. 115861), EBOMAN (grant nr. 115850)
            and EBODAC (grant nr. 115847). This Joint Undertaking receives
            support from the European Union’s Horizon 2020 research
            and innovation programme and EFPIA. {'\n'}
            <Text style={{color: 'blue'}} onPress={ ()=> Linking.openURL('https://www.imi.europa.eu') } >www.imi.europa.eu</Text>
          </Text>}
        </View>
    );
  }
}

export default Footer;