import ProjectsMenuView from './menu-view.js'
import Model from './model.js'
import View from './page-view.js';
import ProjectTestsPage from '../pages/project-tests.js'

const model = new Model()
const view = new View('main')
const menu = new ProjectsMenuView()
const page = new ProjectTestsPage()


menu.render(model)

const tests = ({ blId, prId, prev }) => {
  const lineId = parseInt(blId)
  const projectId = parseInt(prId)

  const project = model.project(lineId, projectId)
  const testsData = model.projectTests(lineId, projectId)

  if (project && project.name) {
    return view.render('testsInProject', project.name, testsData)
  } else {
    return view.render('notFoundError', blId, prId)
  }
}

const error = ({ from, to }) => view.render('routeError', from, to)

function stay ({ prev }) {
  location.hash = prev
  return view.render('emptyPage')
}

function activeMenuItem (route, { blId, prId }) {
  if (route !== 'error') {
    menu.setActiveProject(blId, prId)
  }
}

export default {
  tests, error, stay, activeMenuItem
}