package ni.edu.uccleon

class ReportController {
    RequestService requestService

    static defaultAction = 'resumen'

    def resumen(final Integer year) {
        [results: requestService.resumen(year), yearFilter: createYearFilter()]
    }

    def reportSummary(final Integer month, final Integer year) {
        [results: requestService.reportSummary(month, year)]
    }

    def summaryByCoordination(final String school, final Integer month, final Integer year) {
        [results: requestService.summaryByCoordination(school, month, year)]
    }

    def summaryByUser(final Long userId, final String school, final Integer month, final Integer year) {
        [results: requestService.summaryByUser(userId, school, month, year)]
    }

    def reportPerDay(final Integer year) {
        [results: requestService.reportPerDay(year), yearFilter: createYearFilter()]
    }

    def reportByBlock(final Integer year) {
        [results: requestService.reportByBlock(year), yearFilter: createYearFilter()]
    }

    def reportByDatashows(final Integer year) {
        [results: year ? requestService.getProjectorReportByYear(year) : requestService.getProjectorReport(), yearFilter: createYearFilter()]
    }

    def reportByClassrooms(final Integer year) {
        [results: year ? requestService.classReportPerYear(year) : requestService.classReport(), yearFilter: createYearFilter()]
    }

    def reportBySchool(final Integer year) {
        [results: year ? requestService.coordinationReportByYear(year) : requestService.coordinationReport(), yearFilter: createYearFilter()]
    }

    def coordinationSummary(final String school, final Integer year) {
        [results: year ? requestService.coordinationSummaryInYear(school, year) : requestService.coordinationSummary(school)]
    }

    def coordinationSummaryInMonth(final String school, final Integer month, final Integer year) {
        [results: requestService.summaryByCoordination(school, month, year)]
    }

    def summaryOfApplicants(final Integer year) {
        [results: year ? requestService.summaryByApplicantInYear(year) : requestService.summaryByApplicant(), yearFilter: createYearFilter()]
    }

    def summaryOfApplicationsPerApplicant(final String fullName, final Long id, final Integer year) {
        [results: year ? requestService.summaryOfApplicationsPerApplicantInYear(id, year) : requestService.summaryOfApplicationsPerApplicant(id)]
    }

    def coordinationList() {
        [coordinationList: session.user.schools]
    }

    def facultySummary(final String school, final Integer year) {
        [results: year ? requestService.annualFacultySummary(school, year) : requestService.globalFacultySummary(school), schoolYearFilter: createSchoolYearFilter(school)]
    }

    def summaryOfTeacherApplicationsInMonth(final String school, final Integer month, final Integer year) {
        [results: requestService.summaryByCoordination(school, month, year)]
    }

    private YearFilter createYearFilter() {
        new YearFilter(years: requestService.getYearsOfApplications())
    }

    private SchoolYearFilter createSchoolYearFilter(final String school) {
        new SchoolYearFilter(yearList: requestService.getYearListByFaculty(school))
    }
}

class YearFilter {
    List<Integer> years
}

class SchoolYearFilter {
    List<Integer> yearList
}
