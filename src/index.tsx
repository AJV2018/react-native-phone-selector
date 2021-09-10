import { NativeModules } from 'react-native';

type PhoneSelectorType = {
  multiply(a: number, b: number): Promise<number>;
};

const { PhoneSelector } = NativeModules;

export default PhoneSelector as PhoneSelectorType;
