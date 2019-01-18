import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  Image,
  TouchableOpacity,
  ImageBackground,
  AsyncStorage,
  ToastAndroid
} from 'react-native';

import FloatingLabel from 'react-native-floating-labels';
import AuthenticationService from '../services/Authentication';
import RegisterValidator from '../validators/Register';

export default class Login extends Component {
  static navigationOptions = {
    header: null
  };

  state = {
    username: '',
    password: ''
  };

  constructor() {
    super();
    this.auth = AuthenticationService.getInstance();
  }

  buttonLoginPressed() {
    try {
      RegisterValidator.validate(
        this.state.username,
        this.state.password,
        this.state.password
      );

      const that = this;
      this.auth
        .login(this.state.username, this.state.password)
        .then(result => {
          if (result.ok) {
            AsyncStorage.setItem('username', that.state.username);
            AsyncStorage.setItem('jwt', result.headers.map.authentication);
            that.props.navigation.navigate('HomeScreen');
            return;
          }

          ToastAndroid.showWithGravity(
            'Wrong username or password',
            ToastAndroid.LONG,
            ToastAndroid.TOP
          );
        })
        .catch(erro => {
          ToastAndroid.showWithGravity(
            erro.message,
            ToastAndroid.LONG,
            ToastAndroid.TOP
          );
        });
    } catch (exception) {
      ToastAndroid.showWithGravity(
        exception.message,
        ToastAndroid.LONG,
        ToastAndroid.TOP
      );
    }
  }

  buttonRegisterPressed() {
    this.props.navigation.navigate('RegisterScreen');
  }

  async componentDidMount() {
    const value = await AsyncStorage.getItem('username');

    if (value !== null) {
      this.props.navigation.navigate('HomeScreen');
      return;
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
              source={require('../images/avatar.png')}
            />
          </View>
          <View>
            <Text
              style={{
                color: 'white',
                fontSize: 20,
                paddingTop: 15
              }}
            >
              Log in to continue
            </Text>
          </View>
        </View>
        <View style={styles.formContainer}>
          <FloatingLabel
            labelStyle={styles.labelInput}
            inputStyle={styles.input}
            style={styles.formInput}
            onChangeText={value => this.setState({ username: value })}
          >
            Enter username
          </FloatingLabel>
          <FloatingLabel
            secureTextEntry={true}
            labelStyle={styles.labelInput}
            inputStyle={styles.input}
            style={styles.formInput}
            onChangeText={value => this.setState({ password: value })}
          >
            Enter password
          </FloatingLabel>
        </View>

        <View style={styles.buttonContainers}>
          <View style={styles.buttonHolder}>
            <TouchableOpacity
              style={styles.buttonLogin}
              onPress={() => this.buttonLoginPressed()}
            >
              <Text style={styles.inputButton}>LOG IN</Text>
            </TouchableOpacity>
          </View>
          <View>
            <Text style={styles.inputAccount}>Not Having an Accout?</Text>
          </View>
          <View style={styles.buttonHolder}>
            <TouchableOpacity
              style={styles.buttonRegister}
              onPress={() => this.buttonRegisterPressed()}
            >
              <Text style={styles.inputButton}>REGISTER </Text>
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
    paddingTop: 70,
    alignItems: 'center',
    backgroundColor: 'transparent'
  },
  buttonContainers: {
    flex: 1,
    justifyContent: 'center'
  },
  image: {
    width: 100,
    height: 100
  },
  labelInput: {
    color: 'white'
  },
  formInput: {
    borderBottomWidth: 1,
    marginLeft: 70,
    marginRight: 70,
    marginTop: 40,
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
