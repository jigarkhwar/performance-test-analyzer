import Router from './core/router.js'
import Controller from './core/controller.js'


const r = new Router()
  .addRoute(Controller.stay)
  .addRoute('error', Controller.error )
  .addRoute('bis-line/:blId/project/:prId', Controller.tests)
  .afterProcessRoute(Controller.activeMenuItem)
  .run()
