import React, { Component } from 'react';
import { View, Animated, Text, TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import PropTypes from 'prop-types';

const styles = {
  container: {
    backgroundColor: '#fff',
    overflow: 'hidden',
  },
  titleContainer: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    borderBottomColor: '#e7e7e7',
    borderBottomWidth: 1,
    height: 50,
  },
  title: {
    fontSize: 20,
    color: '#337ab7',
    paddingLeft: 5,
  },
  iconContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    marginLeft: 10,
    width: 30,
  },
  stretchToRight: {
    marginLeft: 'auto',
    marginRight: 10,
  },
};

class Collapsible extends Component {
  constructor(props) {
    super(props);

    this.state = {
      title: props.title,
      headerIcon: props.headerIcon,
      expanded: false,
      animation: new Animated.Value(50),
    };

    this.toggle = this.toggle.bind(this);
    this.setMinHeight = this.setMinHeight.bind(this);
    this.setMaxHeight = this.setMaxHeight.bind(this);
  }

  setMaxHeight(event) {
    this.setState({
      maxHeight: event.nativeEvent.layout.height,
    });
  }

  setMinHeight(event) {
    this.setState({
      minHeight: event.nativeEvent.layout.height,
    });
  }

  toggle() {
    const initialValue = this.state.expanded ?
      this.state.maxHeight + this.state.minHeight : this.state.minHeight;
    const finalValue = this.state.expanded ?
      this.state.minHeight : this.state.maxHeight + this.state.minHeight;

    this.setState({
      expanded: !this.state.expanded,
    });

    this.state.animation.setValue(initialValue);
    Animated.spring(
      this.state.animation,
      {
        toValue: finalValue,
      },
    ).start();
  }

  render() {
    let icon = 'angle-left';

    if (this.state.expanded) {
      icon = 'angle-down';
    }

    return (
      <Animated.View style={[styles.container, { height: this.state.animation }]}>
        <TouchableOpacity onPress={this.toggle}>
          <View style={styles.titleContainer} onLayout={this.setMinHeight}>
            <View style={styles.iconContainer}>
              <Icon name={this.state.headerIcon} size={20} color="#337ab7" />
            </View>
            <Text style={styles.title}>{this.state.title}</Text>
            <Icon name={icon} size={24} color="#337ab7" style={styles.stretchToRight} />
          </View>
        </TouchableOpacity>
        <View onLayout={this.setMaxHeight}>
          {this.props.children}
        </View>
      </Animated.View>
    );
  }
}

export default Collapsible;

Collapsible.propTypes = {
  children: PropTypes.element.isRequired,
  title: PropTypes.string.isRequired,
  headerIcon: PropTypes.string.isRequired,
};
