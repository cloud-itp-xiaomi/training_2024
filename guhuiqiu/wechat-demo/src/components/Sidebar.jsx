// eslint-disable-next-line no-unused-vars
import React from 'react'
import Navbar from './Navbar'
import Chats from './Chats'

const Sidebar = () => {
  return (
    <div className='sidebar'>
          <Navbar />
          <Chats/>
    </div>
  )
}

export default Sidebar