import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  ImageBackground,
  Text,
  AsyncStorage
} from 'react-native';
import Pie from 'react-native-pie';
import ContactService from '../services/Contacts';

export default class PieChart extends Component {
  static navigationOptions = {
    title: 'User top 3 contacts by first letter'
  };

  constructor() {
    super();
    this.state = {
      percent: [0, 0, 0],
      text: ['Chart for letter ', 'Chart for letter ', 'Chart for letter ']
    };
  }

  async componentDidMount() {
    const token = await AsyncStorage.getItem('jwt');
    const user = await AsyncStorage.getItem('username');

    this.setState(prev => ({
      username: user,
      jwt: token
    }));

    this.getContacts();
  }

  getContacts() {
    that = this;
    ContactService.getInstance()
      .getAllContacts(this.state.jwt)
      .then(resp => {
        let map = new Map();
        let arr = [];

        for (let contact of resp.contacts) {
          const first = contact.firstName.toUpperCase()[0];
          if (!map.get(first)) {
            map.set(first, 0);
          }
          map.set(first, map.get(first) + 1);
        }

        for (let [k, v] of map) {
          arr.push({
            K: k,
            v: v
          });
        }

        arr.sort(function(a, b) {
          return b.v - a.v;
        });

        let sum = 0;
        for (let value of arr.slice(0, 3)) {
          sum += value.v;
        }

        that.setState(prev => {
          for (let index in arr) {
            prev.percent[index] = Math.round((arr[index].v / sum) * 100);
            prev.text[index] = 'Chart for letter ' + arr[index].K;
          }
          return prev;
        });
      });
  }

  render() {
    return (
      <ImageBackground
        source={require('../images/back1.jpg')}
        style={styles.container}
      >
        <View style={styles.view}>
          <Pie
            radius={70}
            innerRadius={65}
            series={[this.state.percent[0]]}
            colors={['#f5f5f5']}
            backgroundColor="#980000"
          />
          <View style={styles.gauge}>
            <Text style={styles.gaugeText}>{this.state.percent[0]}%</Text>
          </View>
          <View>
            <Text style={styles.gaugeTitle}>{this.state.text[0]}</Text>
          </View>
        </View>

        <View style={styles.view}>
          <Pie
            radius={70}
            innerRadius={65}
            series={[this.state.percent[1]]}
            colors={['#f5f5f5']}
            backgroundColor="#980000"
          />
          <View style={styles.gauge}>
            <Text style={styles.gaugeText}>{this.state.percent[1]}%</Text>
          </View>
          <View>
            <Text style={styles.gaugeTitle}>{this.state.text[1]}</Text>
          </View>
        </View>

        <View style={styles.view}>
          <Pie
            radius={70}
            innerRadius={65}
            series={[this.state.percent[2]]}
            colors={['#f5f5f5']}
            backgroundColor="#980000"
          />
          <View style={styles.gauge}>
            <Text style={styles.gaugeText}>{this.state.percent[2]}%</Text>
          </View>
          <View>
            <Text style={styles.gaugeTitle}>{this.state.text[2]}</Text>
          </View>
        </View>
      </ImageBackground>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'space-around'
  },
  view: {
    position: 'relative'
  },
  gauge: {
    position: 'absolute',
    width: 140,
    height: 140,
    alignItems: 'center',
    justifyContent: 'center'
  },
  gaugeText: {
    backgroundColor: 'transparent',
    color: 'white',
    fontSize: 24
  },
  gaugeTitle: {
    backgroundColor: 'transparent',
    color: 'white',
    fontSize: 18,
    marginTop: 10
  }
});
