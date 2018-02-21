import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {License, LicenseList, LicenseService} from "../license.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-list',
    templateUrl: './list.component.html',
    styleUrls: ['./list.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class ListComponent implements OnInit {

    pageSizes = [5, 10, 25, 50];
    itemsPerPage = 25;
    currentPage = 0;
    totalPages = 0;
    totalResultsCount = 0;
    currentResultsCount = 0;
    allLoaded = false;

    licenses: License[] = [];
    searchTerm: string;

    constructor(private licenseService: LicenseService,
        private router: Router) {
    }

    ngOnInit() {
        this.currentPage = 0;
        this.licenseService.getLicenses(this.itemsPerPage, this.itemsPerPage * this.currentPage).subscribe(l => this.loadLicenseList(l));
    }

    searchForLicenses() {
        if (this.searchTerm === undefined || this.searchTerm == null || this.searchTerm.length <= 0) {
            this.licenseService.getLicenses(this.itemsPerPage, this.itemsPerPage * this.currentPage).subscribe(l => this.loadLicenseList(l));
        }
        else {
            this.licenseService.findLicenses(this.searchTerm, this.itemsPerPage, this.itemsPerPage * this.currentPage)
                .subscribe(l => this.loadLicenseList(l));
        }
    }

    private loadLicenseList(licenses: LicenseList) {
        this.licenses = licenses.entries;

        this.totalResultsCount = licenses.totalCount;
        this.currentResultsCount = this.licenses.length + this.itemsPerPage * this.currentPage;
        this.totalPages = Math.round(Math.ceil((this.totalResultsCount / this.itemsPerPage)));

        this.allLoaded = this.currentPage >= (this.totalPages - 1);
        //console.log("allLoaded: ", this.allLoaded, "currentResultsCount:", this.currentResultsCount, "totalResultsCount: ", this.totalResultsCount, this.licenses);
    };

    // show items per page
    setPagination() {
        this.currentPage = 0;
        this.searchForLicenses();
    };

    prevPage() {
        if (this.currentPage > 0) {
            this.currentPage--;
        }
        this.searchForLicenses();
    };

    nextPage() {
        if (!this.allLoaded) {
            this.currentPage++;
        }
        this.searchForLicenses();
    };

    setPage(page) {
        this.currentPage = page;
        this.searchForLicenses();
    };

    range(start, end) {
        var ret = [];
        if (!end) {
            end = start;
            start = 0;
        }
        for (var i = start; i < end; i++) {
            ret.push(i);
        }
        return ret;
    };

    view = id => {
        this.router.navigate(["/view", id]);
    };

}
