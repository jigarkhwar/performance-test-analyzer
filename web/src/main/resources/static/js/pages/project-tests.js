import dom from '../utils/dom-manipulation.js'

export default class ProjectTestsPage {
  constructor () {
    this.uploadLogButton = dom('#upload-log-button')
    this.uploadLogButton.on('click', uploadFile)
  }
}

const uploadFile = () => {
  dom('#upload-log-input')
    .map(inp => inp.files[0])
    .map(file => {
      const formData = new FormData()
      formData.append("logSource", file)
      return formData
    }).forEach(data => {
      fetch('/test/upload/file', {
        method: 'POST',
        body: data
      })
        .then(response => response.text())
        .then(( respText) => {
          if(respText === "OK") alert('Success: File uploaded successful')
          else throw new Error("File upload failed")
        })
        .catch(error => {
          console.log(error, error.message)
          alert('Error:' + error.message)
        })
    })

}