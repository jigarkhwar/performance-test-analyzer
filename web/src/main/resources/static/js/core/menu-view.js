import dom from '../utils/dom-manipulation.js'
import templates from '../templates/templates.js'

export default class ProjectsMenuView {
  constructor () {
    this.activeItem = dom('.active')
    this.menu = dom('ul.nav')
  }

  setActiveProject (blId, prId) {
    this.activeItem.toggleClass('active')
    const bl = dom(`#bis-line-${blId}`)

    if (!bl.hasClass('expanded')) {
      bl.toggleClass('expanded')
    }
    this.activeItem = dom(`#bis-line-${blId} li#project-${prId}`).toggleClass('active')
  }

  addBL (businessLineData) {
    const businessLine = templates.menuItem(businessLineData)
    businessLine.children[0]
      .on('click', ({ target }) => {
        const blId = target.closest('.sidebar-heading').parentNode.id.replace('bis-line-', '')
        this.toggleProjectsForBL(blId)
      })
    this.menu.add(businessLine)

  }

  deleteBL (id) {
    this.menu.rm(`#bis-line-${id}`)
  }

  addProjectToBL (blId, projectData) {
    this.menu.qs(`#bis-line-${blId} ul`).add(templates.project(blId, projectData))
    // this.lines[blId].q('ul').append(project(projectData))
  }

  deleteProjectFromBl (blIb, projectId) {
    this.menu.rm(`#bis-line-${blIb} ul li#project-${projectId}`)
  }

  toggleProjectsForBL (blId) {
    this.menu.qs(`#bis-line-${blId}`).toggleClass('expanded')
  }
  
  render(model) {
    model.businessLines.forEach(i => this.addBL(i))
  }
}