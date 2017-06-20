package ni.edu.uccleon

class SendSummaryToManagersJob {
    def requestService

    static triggers = {
        cron name: 'sendSummaryToManagers', cronExpression: '0 30 8 1 * ?'
    }

    def execute() {
        requestService.sendSummaryToManagers()
    }
}
