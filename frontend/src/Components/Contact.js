import React, { Component } from 'react';
//import PropTypes from 'prop-types'
import {Badge} from 'react-bootstrap'

class Contact extends Component{
    constructor(props, context) {
        super(props, context);

        this.state = {
            messages: this.props.messagesCount
        };
    }

    render(){
        return(
            <a href="#" onClick={this.props.onClick}>
                <li className="list-group-item d-flex justify-content-between align-items-center">
                    {this.props.name}
                    <Badge className="badge-primary badge-pill">{this.state.messages}</Badge>
                </li>
            </a>
        );
    }
}

export default Contact;