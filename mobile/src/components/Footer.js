import React from 'react';
import { Linking, Text, View } from 'react-native';

const styles = {
  footerInfo: {
    width: '100%',
    height: 100,
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
  },

  textStyle: {
    textAlign: 'center',
    fontSize: 10,
    backgroundColor: '#e7e7e7',
  },
};

function Footer() {
  return (
    <View style={styles.footerInfo}>
      <Text style={styles.textStyle}>This project has received funding from
        the Innovative Medicines
        Initiative 2 Joint Undertaking under grant agreement EBOVAC1
        (grant nr. 115854), EBOVAC2 (grant nr. 115861), EBOMAN (grant nr.
        115850)
        and EBODAC (grant nr. 115847). This Joint Undertaking receives
        support from the European Union’s Horizon 2020 research
        and innovation programme and EFPIA. {'\n'}
        <Text
          style={{ color: 'blue' }}
          onPress={() => Linking.openURL('https://www.imi.europa.eu')}
        >www.imi.europa.eu
        </Text>
      </Text>
    </View>
  );
}

export default Footer;
