import dom from '../utils/dom-manipulation.js'
import templates from '../templates/templates.js'

export default class PageView{
    constructor(root) {
        this.root = dom(root)
    }
    render(templateName, ...data) {
        this.root.rmChildren()
        const pageHTML = templates[templateName].apply(this, data)
    
        if (pageHTML instanceof Array) {
          pageHTML.forEach(htmlEl => this.root.add(htmlEl))
        } else {
          this.root.add(pageHTML)
        }
    }
};
