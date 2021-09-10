import * as React from 'react';

import { StyleSheet, View, Text, Button, Alert } from 'react-native';
import PhoneSelector from 'react-native-phone-selector';

export default function App() {
  const [result, setResult] = React.useState<string | undefined>();

  React.useEffect(() => {
    getPhoneNumber()
  }, []);

  const getPhoneNumber = () => {
    PhoneSelector.getPhoneNumber().then(setResult).catch(alert);
  }

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <Button title='Select Phone Number' onPress={getPhoneNumber} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
