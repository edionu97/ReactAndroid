import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  Image,
  TouchableOpacity,
  ImageBackground,
  ToastAndroid
} from 'react-native';

import FloatingLabel from 'react-native-floating-labels';
import RegisterValidator from '../validators/Register';
import AuthenticationService from '../services/Authentication';

export default class Register extends Component {
  state = {
    username: '',
    password: '',
    confirm: ''
  };

  constructor() {
    super();
    this.auth = AuthenticationService.getInstance();
  }
  static navigationOptions = {
    header: null
  };

  buttonLoginPressed() {
    this.props.navigation.navigate('LoginScreen');
  }

  buttonRegisterPressed() {
    var that = this;
    try {
      RegisterValidator.validate(
        this.state.username,
        this.state.password,
        this.state.confirm
      );
      this.auth
        .register(this.state.username, this.state.password)
        .then(function(resp) {
          ToastAndroid.showWithGravity(
            resp.message,
            ToastAndroid.LONG,
            ToastAndroid.TOP
          );
          that.buttonLoginPressed();
        })
        .catch(function(e) {
          ToastAndroid.showWithGravity(
            e.message,
            ToastAndroid.LONG,
            ToastAndroid.TOP
          );
        });
    } catch (e) {
      ToastAndroid.showWithGravity(
        e.message,
        ToastAndroid.LONG,
        ToastAndroid.TOP
      );
    }
  }

  render() {
    return (
      <ImageBackground
        source={require('../images/back1.jpg')}
        style={styles.container}
      >
        <View style={styles.imageContainer}>
          <View>
            <Image
              style={styles.image}
              source={require('../images/register.png')}
            />
          </View>
          <View>
            <Text
              style={{
                color: 'white',
                fontSize: 17,
                paddingTop: 15
              }}
            >
              Create a new account
            </Text>
          </View>
        </View>
        <View style={styles.formContainer}>
          <FloatingLabel
            onChangeText={value => this.setState({ username: value })}
            labelStyle={styles.labelInput}
            inputStyle={styles.input}
            style={styles.formInput}
          >
            Enter username
          </FloatingLabel>
          <FloatingLabel
            secureTextEntry={true}
            onChangeText={value => this.setState({ password: value })}
            labelStyle={styles.labelInput}
            inputStyle={styles.input}
            style={styles.formInput}
          >
            Enter password
          </FloatingLabel>
          <FloatingLabel
            secureTextEntry={true}
            onChangeText={value => this.setState({ confirm: value })}
            labelStyle={styles.labelInput}
            inputStyle={styles.input}
            style={styles.formInput}
          >
            Confirm password
          </FloatingLabel>
        </View>

        <View style={styles.buttonContainers}>
          <View style={styles.buttonHolder}>
            <TouchableOpacity
              style={styles.buttonLogin}
              onPress={() => this.buttonRegisterPressed()}
            >
              <Text style={styles.inputButton}>REGISTER</Text>
            </TouchableOpacity>
          </View>
          <View>
            <Text style={styles.inputAccount}>Having an Account?</Text>
          </View>
          <View style={styles.buttonHolder}>
            <TouchableOpacity
              style={styles.buttonRegister}
              onPress={() => this.buttonLoginPressed()}
            >
              <Text style={styles.inputButton}>LOG IN </Text>
            </TouchableOpacity>
          </View>
        </View>
      </ImageBackground>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignSelf: 'stretch',
    backgroundColor: 'transparent',
    justifyContent: 'space-between'
  },
  formContainer: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: 'transparent'
  },
  imageContainer: {
    flex: 0,
    justifyContent: 'flex-end',
    paddingTop: 20,
    alignItems: 'center',
    backgroundColor: 'transparent'
  },
  buttonContainers: {
    flex: 1,
    justifyContent: 'center'
  },
  image: {
    width: 90,
    height: 90
  },
  labelInput: {
    color: 'white'
  },
  formInput: {
    borderBottomWidth: 1,
    marginLeft: 70,
    marginRight: 70,
    marginTop: 25,
    borderColor: 'white'
  },
  input: {
    borderWidth: 0,
    color: 'white'
  },
  inputButton: {
    borderWidth: 0,
    color: 'white',
    fontWeight: 'bold'
  },
  inputAccount: {
    marginTop: 25,
    borderWidth: 0,
    color: 'white',
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 2
  },
  buttonHolder: {
    marginLeft: 60,
    marginRight: 60
  },
  buttonLogin: {
    flex: 1,
    borderRadius: 25,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 25,
    backgroundColor: '#f0932b'
  },
  buttonRegister: {
    flex: 1,
    borderRadius: 25,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 25,
    backgroundColor: '#95afc0'
  }
});
