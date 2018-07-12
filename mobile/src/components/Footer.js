import React, { Component } from 'react';
import { Text, View, Linking} from 'react-native';


const styles = {
  footerInfo: {
    width: '100%',
    height: 100,
    backgroundColor: '#e7e7e7',
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
  }
};


class Footer extends Component {
  render() {
    return (
        <View style={styles.footerInfo}>
          <Text style={styles.textStyle}>This project has received funding from the Innovative Medicines
            Initiative 2 Joint Undertaking under grant agreement EBOVAC1
            (grant nr. 115854), EBOVAC2 (grant nr. 115861), EBOMAN (grant nr. 115850)
            and EBODAC (grant nr. 115847). This Joint Undertaking receives
            support from the European Union’s Horizon 2020 research
            and innovation programme and EFPIA. {'\n'}
            <Text style={{color: 'blue'}} onPress={ ()=> Linking.openURL('https://www.imi.europa.eu') } >www.imi.europa.eu</Text>
          </Text>
        </View>
    );
  }
}

export default Footer;