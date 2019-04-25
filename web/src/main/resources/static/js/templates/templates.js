import _ from '../utils/utils.js'
import { vn, h1, h2, span } from '../utils/vdom.js'


const emptyPage = () => h1('...')

const routeError = (from, to) => h2(
  { 'class': 'text-danger text-center' },
  `No page for route ${to}${from === '' ? '' : ' on transition from ' + from}`
)

const notFoundError = (blId, projectId) => h2(
  { 'class': 'text-danger text-center' },
  `Not found project ${projectId} in business line ${blId}`
)


const tests = (data, rowSize) =>
  _.chunks(data, rowSize)
    .map(t => vn('.row', t.map(testRun)))


const testsInProject = (projectName, testsData, rowSize = 4) => [
  h2(projectName),
  _.nonEmpty(testsData) ? tests(testsData, rowSize) :
    vn('p', `No tests in project ${projectName}`)
].flat()


const testRun = (test) =>
  vn(`#test-${test.id}.card.mb-5`,
    vn('.card-header', test.name),
    vn('ul.list-group.list-group-flush',
      vn('li.list-group-item', `Date - ${_.formatDate(test.date, '.')}`),
      vn('li.list-group-item', `RPS - ${test.rps}`)
    )
  ).wrap('.col-3')


const blHeader = (name) =>
  vn('.sidebar-heading',
    span(name),
    vn('.collapse-button',
      vn('i.far.fa-plus-square'),
      vn('i.far.fa-minus-square'),
    )
  )


const projectInMenu = (blId, data) => vn(`li#project-${data.id}.nav-item`,
  vn('a.nav-link', { href: `#bis-line/${blId}/project/${data.id}` },
    vn('.nav-icon', vn('i.far.fa-file-alt')),
    data.name
  )
)

const projects = (blId, blProjects) =>
  vn(`ul.nav.flex-column.mb-2`, blProjects.map(pr => projectInMenu(blId, pr)))


const menuItem = (businessLine) => vn(`li#bis-line-${businessLine.id}.nav-item`,
  blHeader(businessLine.name),
  projects(businessLine.id, businessLine.projects)
)

export default {
  menuItem, projectInMenu, testRun, routeError, testsInProject, emptyPage, notFoundError
}