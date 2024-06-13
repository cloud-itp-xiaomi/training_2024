import './App.scss'

// eslint-disable-next-line no-unused-vars
import React from 'react'
import Sidebar from './components/Sidebar'
import Chat from './components/Chat'

const App = () => {
  return (
    <div className='App'>
      <div className='container'>
        <Sidebar />
        <Chat/>
      </div>
    </div>
  )
}

export default App
