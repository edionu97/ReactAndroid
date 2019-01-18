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
import ContactService from '../services/Contacts';
import ContactValidator from '../validators/ContactValidator';

export default class UpdateContact extends Component {
  static navigationOptions = {
    header: null
  };

  constructor(props) {
    super(props);
    this.state = {
      first: '',
      last: '',
      jwt: '',
      phone: ''
    };
    this.initial = {};
  }

  async componentDidMount() {
    token = await AsyncStorage.getItem('jwt');
    this.setState({
      jwt: token
    });
    this.setState({
      first: this.props.navigation.state.params.data.firstName,
      last: this.props.navigation.state.params.data.lastName,
      phone: this.props.navigation.state.params.data.phoneNumber
    });
  }

  handleUpdatePressed() {
    try {
      ContactValidator.validate(
        this.state.first,
        this.state.last,
        this.state.phone
      );
      ContactService.getInstance()
        .updateContact(this.state.jwt, {
          first: this.state.first,
          last: this.state.last,
          phone: this.state.phone
        })
        .then(res => {
          const message = res.ok
            ? 'Contact successfuly updated'
            : 'Contact could not be updated';

          if (res.ok) {
            this.props.navigation.navigate('HomeScreen');
            this.props.navigation.state.params.update();
          }

          ToastAndroid.showWithGravity(
            message,
            ToastAndroid.LONG,
            ToastAndroid.BOTTOM
          );
        })
        .catch(err => {
          ToastAndroid.showWithGravity(
            err.message,
            ToastAndroid.LONG,
            ToastAndroid.BOTTOM
          );
        });
    } catch (ex) {
      ToastAndroid.showWithGravity(
        ex.message,
        ToastAndroid.LONG,
        ToastAndroid.BOTTOM
      );
    }
  }

  handleCancelPassed() {
    this.props.navigation.navigate('HomeScreen');
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
              source={require('../images/edit.png')}
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
              Update contact {this.state.first}
            </Text>
          </View>
        </View>
        <View style={styles.formContainer}>
          <FloatingLabel
            labelStyle={styles.labelInput}
            inputStyle={styles.input}
            style={styles.formInput}
            onChangeText={value => this.setState({ last: value })}
          >
            Enter second name
          </FloatingLabel>
          <FloatingLabel
            labelStyle={styles.labelInput}
            inputStyle={styles.input}
            style={styles.formInput}
            onChangeText={value => this.setState({ phone: value })}
          >
            Enter phone number
          </FloatingLabel>
        </View>

        <View style={styles.buttonContainers}>
          <View style={styles.buttonHolder}>
            <TouchableOpacity
              style={styles.buttonLogin}
              onPress={() => this.handleUpdatePressed()}
            >
              <Text style={styles.inputButton}>UPDATE</Text>
            </TouchableOpacity>
          </View>
          <View>
            <Text style={styles.inputAccount}>Have you changed your mind?</Text>
          </View>
          <View style={styles.buttonHolder}>
            <TouchableOpacity
              style={styles.buttonRegister}
              onPress={() => this.handleCancelPassed()}
            >
              <Text style={styles.inputButton}>CANCEL</Text>
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
