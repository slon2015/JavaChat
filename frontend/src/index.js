import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './Components/App';
import * as serviceWorker from './serviceWorker';

var contacts = [
    {
        name: 'Владислав Рогов',
        messageCount: 10,
        chat: [
            'Hello','test','cool chat'
        ]
    },
    {
        name: 'Никита Котельников',
        messageCount: 15,
        chat: [
            'Hi','test','cool chat','Iam Никита Котельников'
        ]
    },
    {
        name: 'Илья Машнилов',
        messageCount: 23,
        chat: [
            'Hi','test','cool chat','Iam Илья Машнилов'
        ]
    },
    {
        name: 'Самир Баринов',
        messageCount: 43,
        chat: [
            'Hi','test','cool chat','Iam Самир Баринов'
        ]
    }
]

ReactDOM.render(<App contacts={contacts}/>, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
