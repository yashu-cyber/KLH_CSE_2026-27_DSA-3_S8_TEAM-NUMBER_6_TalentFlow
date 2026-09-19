import { useEffect, useMemo, useState } from "react";

import {
  LayoutDashboard,
  Upload,
  Users,
  Search,
  GitCompare,
  Copy,
  CalendarDays,
  Settings,
  FileText,
  AlertTriangle,
  CheckCircle2,
  UserRound,
  ArrowUpRight,
  ArrowRight,
  LogIn,
  Sparkles,
  X,
  LoaderCircle,
  ShieldCheck,
  RefreshCw,
  Filter,
  SlidersHorizontal,
  Target,
  Clock3,
  UserCheck,
  Zap,
  BarChart3,
  ChevronRight,
  Check,
} from "lucide-react";

import "./App.css";

const API_BASE = "http://localhost:8080/api";

const menuItems = [
  {
    id: "dashboard",
    label: "Dashboard",
    icon: LayoutDashboard,
  },
  {
    id: "upload",
    label: "Upload Resume",
    icon: Upload,
  },
  {
    id: "candidates",
    label: "Candidates",
    icon: Users,
  },
  {
    id: "search",
    label: "Search",
    icon: Search,
  },
  {
    id: "similarity",
    label: "Similarity",
    icon: GitCompare,
  },
  {
    id: "duplicates",
    label: "Duplicates",
    icon: Copy,
  },
  {
    id: "schedule",
    label: "Schedule Interviews",
    icon: CalendarDays,
  },
  {
    id: "optimization",
    label: "Candidate Optimization",
    icon: Target,
  },
  {
    id: "settings",
    label: "Settings",
    icon: Settings,
  },
];

async function apiFetch(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, options);

  let data;

  try {
    data = await response.json();
  } catch {
    throw new Error("TalentFlow returned an invalid server response.");
  }

  if (!response.ok || data.success === false) {
    throw new Error(
      data.message || "Something went wrong while contacting TalentFlow."
    );
  }

  return data;
}

function App() {
  const [page, setPage] = useState("landing");

  if (page === "landing") {
    return <LandingPage setPage={setPage} />;
  }

  if (page === "login") {
    return <LoginPage setPage={setPage} />;
  }

  return (
    <div className="app dashboard-layout">
      <Sidebar page={page} setPage={setPage} />

      <main className="dashboard-main">
        <header className="dashboard-header">
          <div className="global-search">
            <Search size={17} strokeWidth={1.8} />

            <input
              placeholder="Search candidates, skills, or jobs..."
              onKeyDown={(event) => {
                if (event.key === "Enter") {
                  setPage("search");
                }
              }}
            />
          </div>

          <div className="recruiter-menu">
            <div className="recruiter-avatar">Y</div>

            <span>Recruiter</span>

            <span className="chevron">⌄</span>
          </div>
        </header>

        <section className="dashboard-content">
          {page === "dashboard" && (
            <DashboardHome setPage={setPage} />
          )}

          {page === "upload" && <UploadPage />}

          {page === "candidates" && (
            <CandidatesPage setPage={setPage} />
          )}

          {page === "search" && (
            <SearchPage setPage={setPage} />
          )}

          {page === "similarity" && (
            <SimilarityPage />
          )}

          {page === "duplicates" && (
            <DuplicatesPage />
          )}

          {page === "schedule" && (
            <SchedulePage />
          )}

          {page === "optimization" && (
            <OptimizationPage />
          )}

          {page === "settings" && (
            <SettingsPage />
          )}
        </section>
      </main>
    </div>
  );
}

/* =========================================================
   LANDING
========================================================= */

function LandingPage({ setPage }) {
  return (
    <div className="app landing-page">
      <nav className="landing-nav">
        <div className="brand">TalentFlow</div>

        <div className="landing-links">
          <span>Home</span>
          <span>Features</span>
          <span>About</span>
        </div>

        <div className="landing-actions">
          <button
            className="outline-button"
            onClick={() => setPage("login")}
          >
            Login
          </button>

          <button
            className="filled-button"
            onClick={() => setPage("login")}
          >
            Get Started
            <ArrowRight size={15} strokeWidth={1.8} />
          </button>
        </div>
      </nav>

      <main className="landing-hero">
        <div className="landing-copy">
          <p className="eyebrow">SMART RECRUITMENT WORKSPACE</p>

          <h1>
            Find the
            <br />
            Right Talent,
            <br />
            Faster.
          </h1>

          <p className="landing-description">
            Resume screening, candidate search, comparison,
            optimization, and interview scheduling — all in one place.
          </p>

          <div className="landing-buttons">
            <button
              className="filled-button large-button"
              onClick={() => setPage("login")}
            >
              Get Started
              <ArrowRight size={16} />
            </button>

            <button
              className="outline-button large-button"
              onClick={() => setPage("login")}
            >
              Explore Workspace
            </button>
          </div>
        </div>

        <div className="landing-visual">
          <div className="resume-stack">
            <div className="resume-back"></div>

            <div className="resume-card">
              <div className="candidate-top">
                <div className="candidate-avatar">
                  <UserRound size={26} strokeWidth={1.5} />
                </div>

                <div>
                  <div className="candidate-title">Candidate</div>
                  <div className="fake-line medium"></div>
                </div>
              </div>

              <div className="fake-line long"></div>
              <div className="fake-line medium"></div>

              <div className="skill-tags">
                <span>Python</span>
                <span>SQL</span>
                <span>Java</span>
                <span>Git</span>
              </div>

              <div className="fake-line long"></div>
              <div className="fake-line long"></div>
              <div className="fake-line medium"></div>
            </div>
          </div>

          <div className="match-card">
            <span className="match-label">Candidate Match</span>

            <strong>92%</strong>

            <div className="check-item">
              <CheckCircle2 size={14} />
              <span>Relevant skills</span>
            </div>

            <div className="check-item">
              <CheckCircle2 size={14} />
              <span>Relevant experience</span>
            </div>

            <div className="check-item">
              <CheckCircle2 size={14} />
              <span>No duplicate resume</span>
            </div>

            <div className="check-item">
              <CheckCircle2 size={14} />
              <span>Interview ready</span>
            </div>
          </div>

          <div className="visual-note">
            From Resume
            <br />
            to Results
          </div>
        </div>
      </main>
    </div>
  );
}

/* =========================================================
   LOGIN
========================================================= */

function LoginPage({ setPage }) {
  return (
    <div className="app login-page">
      <div className="login-brand">
        <div className="brand">TalentFlow</div>

        <h1>
          Welcome Back,
          <br />
          Recruiter!
        </h1>

        <p>
          Sign in to continue to your TalentFlow
          recruitment workspace.
        </p>

        <div className="login-benefits">
          <div>
            <ShieldCheck size={18} />
            <span>Secure recruiter access</span>
          </div>

          <div>
            <Sparkles size={18} />
            <span>Structured candidate screening</span>
          </div>

          <div>
            <Users size={18} />
            <span>Centralized candidate management</span>
          </div>
        </div>
      </div>

      <div className="login-card">
        <p className="login-label">RECRUITER LOGIN</p>

        <form
          onSubmit={(event) => {
            event.preventDefault();
            setPage("dashboard");
          }}
        >
          <label>Email</label>

          <input
            type="email"
            placeholder="recruiter@example.com"
            required
          />

          <label>Password</label>

          <input
            type="password"
            placeholder="Enter your password"
            required
          />

          <button
            className="filled-button login-button"
            type="submit"
          >
            <LogIn size={16} />
            Login
          </button>
        </form>

        <div className="login-divider">
          <span>or</span>
        </div>

        <p className="demo-text">
          Demo access · Use any valid email and password
        </p>
      </div>
    </div>
  );
}

/* =========================================================
   SIDEBAR
========================================================= */

function Sidebar({ page, setPage }) {
  return (
    <aside className="sidebar">
      <div className="sidebar-brand">TalentFlow</div>

      <nav className="sidebar-nav">
        {menuItems.map((item) => {
          const Icon = item.icon;

          return (
            <button
              key={item.id}
              className={
                page === item.id
                  ? "sidebar-item active"
                  : "sidebar-item"
              }
              onClick={() => setPage(item.id)}
            >
              <span className="sidebar-icon">
                <Icon size={18} strokeWidth={1.8} />
              </span>

              <span className="sidebar-label">
                {item.label}
              </span>
            </button>
          );
        })}
      </nav>
    </aside>
  );
}

/* =========================================================
   DASHBOARD
========================================================= */

function DashboardHome({ setPage }) {
  const [dashboard, setDashboard] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadDashboard = async () => {
    try {
      setLoading(true);
      setError("");

      const data = await apiFetch("/dashboard");

      setDashboard(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDashboard();
  }, []);

  const totalResumes = dashboard?.totalResumes ?? 0;
  const uniqueCandidates = dashboard?.uniqueCandidates ?? 0;
  const duplicatesRemoved = dashboard?.duplicatesRemoved ?? 0;
  const interviewSlotsFilled =
    dashboard?.interviewSlotsFilled ?? 0;

  return (
    <>
      <div className="page-heading-row">
        <div className="page-heading">
          <p className="eyebrow">RECRUITMENT OVERVIEW</p>

          <h1>Welcome back, Recruiter!</h1>

          <p>
            Here&apos;s an overview of your recruitment pipeline.
          </p>
        </div>

        <button
          className="icon-button"
          onClick={loadDashboard}
          title="Refresh dashboard"
        >
          <RefreshCw
            size={17}
            className={loading ? "spin" : ""}
          />
        </button>
      </div>

      {error && <ErrorBanner message={error} />}

      <div className="stats-grid">
        <StatCard
          number={loading ? "—" : totalResumes}
          label="Total Resumes"
          icon={FileText}
        />

        <StatCard
          number={loading ? "—" : uniqueCandidates}
          label="Unique Candidates"
          icon={Users}
        />

        <StatCard
          number={loading ? "—" : duplicatesRemoved}
          label="Duplicates Removed"
          icon={Copy}
        />

        <StatCard
          number={loading ? "—" : interviewSlotsFilled}
          label="Interview Slots Filled"
          icon={CalendarDays}
        />
      </div>

      <div className="dashboard-grid">
        <div className="panel">
          <div className="panel-header">
            <div>
              <h2>Recent Activity</h2>
              <p>Current workspace activity.</p>
            </div>
          </div>

          <div className="activity-list">
            <Activity
              icon={FileText}
              title="Resume processing"
              text={`${totalResumes} resumes currently in the workspace.`}
            />

            <Activity
              icon={Copy}
              title="Duplicate detection"
              text={`${duplicatesRemoved} duplicate files identified.`}
            />

            <Activity
              icon={CalendarDays}
              title="Interview scheduling"
              text={`${interviewSlotsFilled} interview slots currently filled.`}
            />

            <Activity
              icon={Users}
              title="Candidate pool"
              text={`${uniqueCandidates} unique candidates available.`}
            />
          </div>
        </div>

        <div className="panel">
          <div className="panel-header">
            <div>
              <h2>Quick Actions</h2>
              <p>Jump directly into your workflow.</p>
            </div>
          </div>

          <div className="quick-actions">
            <ActionButton
              icon={Upload}
              label="Upload Resume"
              onClick={() => setPage("upload")}
            />

            <ActionButton
              icon={Search}
              label="Search Candidates"
              onClick={() => setPage("search")}
            />

            <ActionButton
              icon={GitCompare}
              label="Compare Resumes"
              onClick={() => setPage("similarity")}
            />

            <ActionButton
              icon={CalendarDays}
              label="Schedule Interviews"
              onClick={() => setPage("schedule")}
            />

            <ActionButton
              icon={Target}
              label="Optimize Candidate Selection"
              onClick={() => setPage("optimization")}
            />
          </div>
        </div>
      </div>
    </>
  );
}

/* =========================================================
   UPLOAD
========================================================= */

function UploadPage() {
  const [files, setFiles] = useState([]);
  const [isDragging, setIsDragging] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);

  const addFiles = (selectedFiles) => {
    const pdfFiles = Array.from(selectedFiles).filter(
      (file) =>
        file.type === "application/pdf" ||
        file.name.toLowerCase().endsWith(".pdf")
    );

    setFiles((current) => {
      const existingKeys = new Set(
        current.map(
          (item) =>
            `${item.file.name}-${item.file.size}-${item.file.lastModified}`
        )
      );

      const newFiles = pdfFiles
        .filter((file) => {
          const key =
            `${file.name}-${file.size}-${file.lastModified}`;

          return !existingKeys.has(key);
        })
        .map((file) => ({
          id:
            `${file.name}-${file.size}-${file.lastModified}`,
          file,
          status: "ready",
          candidate: null,
          error: null,
        }));

      return [...current, ...newFiles];
    });
  };

  const handleFileSelect = (event) => {
    addFiles(event.target.files);
    event.target.value = "";
  };

  const handleDrop = (event) => {
    event.preventDefault();
    setIsDragging(false);
    addFiles(event.dataTransfer.files);
  };

  const removeFile = (id) => {
    if (isProcessing) return;

    setFiles((current) =>
      current.filter((item) => item.id !== id)
    );
  };

  const uploadSingleFile = async (item) => {
    setFiles((current) =>
      current.map((fileItem) =>
        fileItem.id === item.id
          ? {
              ...fileItem,
              status: "processing",
              error: null,
            }
          : fileItem
      )
    );

    try {
      const formData = new FormData();

      formData.append("file", item.file);

      const data = await apiFetch("/upload", {
        method: "POST",
        body: formData,
      });

      setFiles((current) =>
        current.map((fileItem) =>
          fileItem.id === item.id
            ? {
                ...fileItem,
                status: "success",
                candidate: data.candidate,
                error: null,
              }
            : fileItem
        )
      );
    } catch (error) {
      setFiles((current) =>
        current.map((fileItem) =>
          fileItem.id === item.id
            ? {
                ...fileItem,
                status: "error",
                error: error.message,
              }
            : fileItem
        )
      );
    }
  };

  const processAll = async () => {
    if (isProcessing) return;

    const pendingFiles = files.filter(
      (item) =>
        item.status === "ready" ||
        item.status === "error"
    );

    if (pendingFiles.length === 0) return;

    setIsProcessing(true);

    const batchSize = 4;

    for (
      let i = 0;
      i < pendingFiles.length;
      i += batchSize
    ) {
      const batch = pendingFiles.slice(
        i,
        i + batchSize
      );

      await Promise.all(
        batch.map((item) =>
          uploadSingleFile(item)
        )
      );
    }

    setIsProcessing(false);
  };

  const totalFiles = files.length;

  const processedFiles = files.filter(
    (item) => item.status === "success"
  ).length;

  const failedFiles = files.filter(
    (item) => item.status === "error"
  ).length;

  const processingFiles = files.filter(
    (item) => item.status === "processing"
  ).length;

  const hasReadyFiles = files.some(
    (item) =>
      item.status === "ready" ||
      item.status === "error"
  );

  const progress =
    totalFiles === 0
      ? 0
      : Math.round(
          ((processedFiles + failedFiles) /
            totalFiles) *
            100
        );

  return (
    <div className="page-shell">
      <div className="page-heading">
        <p className="eyebrow">TALENT PIPELINE</p>

        <h1>Upload Resumes</h1>

        <p>
          Upload one or multiple PDF resumes and let
          TalentFlow process the candidate information.
        </p>
      </div>

      <div
        className={
          isDragging
            ? "upload-dropzone dragging"
            : "upload-dropzone"
        }
        onDragOver={(event) => {
          event.preventDefault();
          setIsDragging(true);
        }}
        onDragLeave={() => setIsDragging(false)}
        onDrop={handleDrop}
      >
        <div className="upload-drop-icon">
          <Upload size={28} strokeWidth={1.6} />
        </div>

        <h2>Drop resumes here</h2>

        <p>Upload multiple PDF resumes at once</p>

        <label className="filled-button upload-select-button">
          <Upload size={16} />
          Choose Files

          <input
            type="file"
            accept=".pdf,application/pdf"
            multiple
            onChange={handleFileSelect}
            hidden
          />
        </label>

        <span className="upload-format">
          PDF files only
        </span>
      </div>

      {totalFiles > 0 && (
        <div className="panel upload-summary">
          <div className="upload-summary-top">
            <div>
              <p className="eyebrow">RESUME QUEUE</p>

              <h2>
                {totalFiles} resume
                {totalFiles !== 1 ? "s" : ""} selected
              </h2>
            </div>

            <button
              className="filled-button"
              onClick={processAll}
              disabled={
                !hasReadyFiles ||
                isProcessing
              }
            >
              {isProcessing ? (
                <>
                  <LoaderCircle
                    size={17}
                    className="spin"
                  />
                  Processing...
                </>
              ) : (
                <>
                  <Upload size={17} />
                  Process All Resumes
                </>
              )}
            </button>
          </div>

          {(isProcessing ||
            processedFiles > 0 ||
            failedFiles > 0) && (
            <div className="upload-progress-section">
              <div className="upload-progress-label">
                <span>
                  {processedFiles +
                    failedFiles}{" "}
                  of {totalFiles} processed
                </span>

                <strong>{progress}%</strong>
              </div>

              <div className="upload-progress-track">
                <div
                  className="upload-progress-bar"
                  style={{
                    width: `${progress}%`,
                  }}
                />
              </div>

              <div className="upload-counts">
                <span>
                  <CheckCircle2 size={14} />
                  {processedFiles} Processed
                </span>

                {processingFiles > 0 && (
                  <span>
                    <LoaderCircle
                      size={14}
                      className="spin"
                    />
                    {processingFiles} Processing
                  </span>
                )}

                {failedFiles > 0 && (
                  <span>
                    <AlertTriangle size={14} />
                    {failedFiles} Failed
                  </span>
                )}
              </div>
            </div>
          )}
        </div>
      )}

      {files.length > 0 && (
        <div className="panel upload-results">
          <div className="panel-header">
            <div>
              <h2>Resume Queue</h2>
              <p>
                Processed resumes become available throughout
                the TalentFlow workspace.
              </p>
            </div>
          </div>

          <div className="upload-file-list">
            {files.map((item) => (
              <UploadFileRow
                key={item.id}
                item={item}
                onRemove={removeFile}
              />
            ))}
          </div>
        </div>
      )}

      {files.length === 0 && (
        <div className="upload-info-grid">
          <InfoCard
            icon={FileText}
            title="Bulk Processing"
            text="Select multiple PDF resumes and process them with one action."
          />

          <InfoCard
            icon={Search}
            title="Candidate Pool"
            text="Successfully processed resumes become searchable candidates."
          />

          <InfoCard
            icon={ShieldCheck}
            title="Structured Extraction"
            text="Candidate information is extracted directly from each resume."
          />
        </div>
      )}
    </div>
  );
}

function UploadFileRow({ item, onRemove }) {
  const candidate = item.candidate;

  return (
    <div className="upload-file-row">
      <div className="upload-file-icon">
        <FileText size={21} strokeWidth={1.7} />
      </div>

      <div className="upload-file-main">
        <div className="upload-file-name">
          {item.file.name}
        </div>

        <div className="upload-file-meta">
          {formatFileSize(item.file.size)}
        </div>

        {item.status === "success" &&
          candidate && (
            <div className="candidate-upload-result">
              <span className="candidate-result-name">
                <UserRound size={14} />
                {candidate.name || "Candidate"}
              </span>

              {candidate.email && (
                <span className="candidate-result-email">
                  {candidate.email}
                </span>
              )}
            </div>
          )}

        {item.status === "error" && (
          <span className="upload-error-text">
            {item.error}
          </span>
        )}
      </div>

      <div className="upload-file-status">
        {item.status === "ready" && (
          <span className="status-ready">
            Waiting
          </span>
        )}

        {item.status === "processing" && (
          <span className="status-processing">
            <LoaderCircle
              size={17}
              className="spin"
            />
            Processing
          </span>
        )}

        {item.status === "success" && (
          <span className="status-success">
            <CheckCircle2 size={17} />
            Processed
          </span>
        )}

        {item.status === "error" && (
          <span className="status-error">
            <AlertTriangle size={16} />
            Failed
          </span>
        )}

        <button
          className="remove-file-button"
          onClick={() => onRemove(item.id)}
          disabled={item.status === "processing"}
        >
          <X size={17} />
        </button>
      </div>
    </div>
  );
}

/* =========================================================
   CANDIDATES
========================================================= */

function CandidatesPage({ setPage }) {
  const [candidates, setCandidates] = useState([]);
  const [query, setQuery] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadCandidates = async () => {
    try {
      setLoading(true);
      setError("");

      const data = await apiFetch("/candidates");

      setCandidates(
        Array.isArray(data.candidates)
          ? data.candidates
          : []
      );
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCandidates();
  }, []);

  const filteredCandidates = useMemo(() => {
    const normalized = query
      .trim()
      .toLowerCase();

    if (!normalized) {
      return candidates;
    }

    return candidates.filter((candidate) => {
      const text = [
        candidate.name,
        candidate.email,
        ...(candidate.skills || []),
      ]
        .join(" ")
        .toLowerCase();

      return text.includes(normalized);
    });
  }, [candidates, query]);

  return (
    <div className="page-shell">
      <div className="page-heading-row">
        <div className="page-heading">
          <p className="eyebrow">CANDIDATE POOL</p>

          <h1>Candidates</h1>

          <p>
            View and manage all processed candidates.
          </p>
        </div>

        <button
          className="outline-button"
          onClick={loadCandidates}
        >
          <RefreshCw size={15} />
          Refresh
        </button>
      </div>

      {error && <ErrorBanner message={error} />}

      <div className="panel">
        <div className="table-toolbar">
          <div className="table-search">
            <Search size={16} />
            <input
              placeholder="Search candidates..."
              value={query}
              onChange={(event) =>
                setQuery(event.target.value)
              }
            />
          </div>

          <div className="table-toolbar-count">
            {filteredCandidates.length} candidates
          </div>
        </div>

        {loading ? (
          <LoadingState text="Loading candidates..." />
        ) : filteredCandidates.length === 0 ? (
          <EmptyState
            icon={Users}
            title="No candidates found"
            text="Upload resumes to build your candidate pool."
          />
        ) : (
          <div className="candidate-table-wrap">
            <table className="candidate-table">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Skills</th>
                  <th>Experience</th>
                  <th>Action</th>
                </tr>
              </thead>

              <tbody>
                {filteredCandidates.map(
                  (candidate, index) => (
                    <tr
                      key={
                        candidate.resumeFile ||
                        `${candidate.name}-${index}`
                      }
                    >
                      <td>{index + 1}</td>

                      <td>
                        <div className="table-name">
                          <div className="mini-avatar">
                            {getInitials(
                              candidate.name
                            )}
                          </div>

                          <strong>
                            {candidate.name ||
                              "Unknown Candidate"}
                          </strong>
                        </div>
                      </td>

                      <td>
                        {candidate.email || "—"}
                      </td>

                      <td>
                        <div className="table-skills">
                          {(candidate.skills || [])
                            .slice(0, 4)
                            .map((skill) => (
                              <span key={skill}>
                                {skill}
                              </span>
                            ))}
                        </div>
                      </td>

                      <td>
                        {formatExperience(
                          candidate.experience
                        )}
                      </td>

                      <td>
                        <button
                          className="small-action-button"
                          onClick={() =>
                            setPage("similarity")
                          }
                        >
                          Compare
                          <ChevronRight size={14} />
                        </button>
                      </td>
                    </tr>
                  )
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

/* =========================================================
   SEARCH
========================================================= */

function SearchPage() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const [error, setError] = useState("");

  const runSearch = async () => {
    if (!query.trim()) {
      return;
    }

    try {
      setLoading(true);
      setError("");
      setSearched(true);

      const data = await apiFetch("/search", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          query: query.trim(),
        }),
      });

      setResults(
        Array.isArray(data.results)
          ? data.results
          : []
      );
    } catch (err) {
      setError(err.message);
      setResults([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-shell">
      <div className="page-heading">
        <p className="eyebrow">CANDIDATE SEARCH</p>

        <h1>Search Candidates</h1>

        <p>
          Search your resume pool using skills, technologies,
          keywords, or phrases.
        </p>
      </div>

      <div className="panel search-panel">
        <div className="search-large">
          <Search size={20} />

          <input
            value={query}
            onChange={(event) =>
              setQuery(event.target.value)
            }
            onKeyDown={(event) => {
              if (event.key === "Enter") {
                runSearch();
              }
            }}
            placeholder="Try Python, Spring Boot, Machine Learning..."
          />

          <button
            className="filled-button"
            onClick={runSearch}
            disabled={
              loading ||
              !query.trim()
            }
          >
            {loading ? (
              <>
                <LoaderCircle
                  size={16}
                  className="spin"
                />
                Searching
              </>
            ) : (
              <>
                <Search size={16} />
                Search
              </>
            )}
          </button>
        </div>
      </div>

      {error && <ErrorBanner message={error} />}

      {searched && !loading && (
        <div className="results-header">
          <div>
            <h2>
              {results.length} result
              {results.length !== 1 ? "s" : ""}
            </h2>

            <p>
              Results for &quot;{query}&quot;
            </p>
          </div>
        </div>
      )}

      {loading && (
        <LoadingState text="Searching the candidate pool..." />
      )}

      {!loading &&
        searched &&
        results.length === 0 && (
          <EmptyState
            icon={Search}
            title="No matching candidates"
            text="Try another skill, technology, or keyword."
          />
        )}

      {!loading && results.length > 0 && (
        <div className="search-results-grid">
          {results.map((candidate, index) => (
            <div
              className="candidate-result-card"
              key={
                candidate.resumeFile ||
                `${candidate.email}-${index}`
              }
            >
              <div className="candidate-card-top">
                <div className="candidate-card-avatar">
                  {getInitials(candidate.name)}
                </div>

                <div>
                  <h3>{candidate.name}</h3>

                  <p>{candidate.email || "No email found"}</p>
                </div>

                <div className="occurrence-badge">
                  {candidate.occurrences} matches
                </div>
              </div>

              <div className="candidate-card-divider"></div>

              <div className="candidate-card-meta">
                <div>
                  <span>Experience</span>
                  <strong>
                    {formatExperience(
                      candidate.experience
                    )}
                  </strong>
                </div>

                <div>
                  <span>Resume</span>
                  <strong>
                    {candidate.resumeFile || "—"}
                  </strong>
                </div>
              </div>

              <div className="candidate-card-skills">
                {(candidate.skills || []).map(
                  (skill) => (
                    <span key={skill}>{skill}</span>
                  )
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

/* =========================================================
   SIMILARITY
========================================================= */

function SimilarityPage() {
  const [candidates, setCandidates] = useState([]);
  const [resumeA, setResumeA] = useState("");
  const [resumeB, setResumeB] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadCandidates = async () => {
      try {
        const data = await apiFetch("/candidates");

        const list = Array.isArray(data.candidates)
          ? data.candidates
          : [];

        setCandidates(list);

        if (list.length >= 2) {
          setResumeA(list[0].resumeFile);
          setResumeB(list[1].resumeFile);
        }
      } catch (err) {
        setError(err.message);
      }
    };

    loadCandidates();
  }, []);

  const compare = async () => {
    if (!resumeA || !resumeB || resumeA === resumeB) {
      setError(
        "Please select two different resumes."
      );
      return;
    }

    try {
      setLoading(true);
      setError("");

      const data = await apiFetch("/similarity", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          resumeA,
          resumeB,
        }),
      });

      setResult(data);
    } catch (err) {
      setError(err.message);
      setResult(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-shell">
      <div className="page-heading">
        <p className="eyebrow">RESUME COMPARISON</p>

        <h1>Resume Similarity</h1>

        <p>
          Compare two resumes across skills, experience,
          and overall profile similarity.
        </p>
      </div>

      {error && <ErrorBanner message={error} />}

      <div className="panel comparison-selector">
        <div className="comparison-field">
          <label>First Resume</label>

          <select
            value={resumeA}
            onChange={(event) =>
              setResumeA(event.target.value)
            }
          >
            <option value="">
              Select a resume
            </option>

            {candidates.map((candidate) => (
              <option
                key={candidate.resumeFile}
                value={candidate.resumeFile}
              >
                {candidate.name} ·{" "}
                {candidate.resumeFile}
              </option>
            ))}
          </select>
        </div>

        <div className="comparison-vs">VS</div>

        <div className="comparison-field">
          <label>Second Resume</label>

          <select
            value={resumeB}
            onChange={(event) =>
              setResumeB(event.target.value)
            }
          >
            <option value="">
              Select a resume
            </option>

            {candidates.map((candidate) => (
              <option
                key={candidate.resumeFile}
                value={candidate.resumeFile}
              >
                {candidate.name} ·{" "}
                {candidate.resumeFile}
              </option>
            ))}
          </select>
        </div>

        <button
          className="filled-button comparison-button"
          onClick={compare}
          disabled={loading}
        >
          {loading ? (
            <>
              <LoaderCircle
                size={16}
                className="spin"
              />
              Comparing
            </>
          ) : (
            <>
              <GitCompare size={16} />
              Compare Resumes
            </>
          )}
        </button>
      </div>

      {result && (
        <div className="similarity-results">
          <div className="similarity-score-card">
            <span>Overall Resume Similarity</span>

            <strong>
              {result.similarityScore}%
            </strong>

            <div className="score-track">
              <div
                className="score-fill"
                style={{
                  width: `${Math.min(
                    100,
                    Math.max(
                      0,
                      result.similarityScore
                    )
                  )}%`,
                }}
              />
            </div>

            <p>
              {result.candidateA} compared with{" "}
              {result.candidateB}
            </p>
          </div>

          <MetricCard
            title="Skills Similarity"
            value={`${result.skillSimilarity}%`}
            icon={Zap}
          />

          <MetricCard
            title="Experience Similarity"
            value={`${result.experienceSimilarity}%`}
            icon={Clock3}
          />

          <div className="panel common-skills-card">
            <div className="panel-header">
              <div>
                <h2>Common Skills</h2>
                <p>
                  Skills present in both resumes.
                </p>
              </div>
            </div>

            <div className="common-skill-list">
              {(result.commonSkills || []).length >
              0 ? (
                result.commonSkills.map((skill) => (
                  <span key={skill}>
                    <Check size={14} />
                    {skill}
                  </span>
                ))
              ) : (
                <span className="muted">
                  No common skills detected.
                </span>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

/* =========================================================
   DUPLICATES
========================================================= */

function DuplicatesPage() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadDuplicates = async () => {
    try {
      setLoading(true);
      setError("");

      const response =
        await apiFetch("/duplicates");

      setData(response);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDuplicates();
  }, []);

  return (
    <div className="page-shell">
      <div className="page-heading-row">
        <div className="page-heading">
          <p className="eyebrow">RESUME QUALITY</p>

          <h1>Duplicate Detection</h1>

          <p>
            Review repeated resume files in your candidate pool.
          </p>
        </div>

        <button
          className="outline-button"
          onClick={loadDuplicates}
        >
          <RefreshCw size={15} />
          Refresh
        </button>
      </div>

      {error && <ErrorBanner message={error} />}

      {loading ? (
        <LoadingState text="Checking resume records..." />
      ) : (
        <>
          <div className="stats-grid duplicate-stats">
            <StatCard
              number={
                data?.totalDuplicateGroups ?? 0
              }
              label="Duplicate Groups"
              icon={Copy}
            />

            <StatCard
              number={
                data?.totalDuplicateFiles ?? 0
              }
              label="Duplicate Files"
              icon={FileText}
            />

            <StatCard
              number={
                data?.duplicateGroups?.length
                  ? "Review"
                  : "Clear"
              }
              label="Workspace Status"
              icon={ShieldCheck}
            />
          </div>

          {(
            data?.duplicateGroups || []
          ).length === 0 ? (
            <EmptyState
              icon={CheckCircle2}
              title="No duplicate resumes found"
              text="Your current candidate pool contains no repeated resume records."
            />
          ) : (
            <div className="duplicate-grid">
              {data.duplicateGroups.map(
                (group, index) => (
                  <div
                    className="panel duplicate-card"
                    key={index}
                  >
                    <div className="duplicate-card-header">
                      <div className="duplicate-number">
                        {index + 1}
                      </div>

                      <div>
                        <h2>
                          Duplicate group
                        </h2>

                        <p>
                          {group.count} matching
                          resume files
                        </p>
                      </div>
                    </div>

                    <div className="duplicate-files">
                      {group.files.map(
                        (file) => (
                          <div
                            className="duplicate-file"
                            key={file}
                          >
                            <FileText size={16} />
                            <span>{file}</span>
                          </div>
                        )
                      )}
                    </div>
                  </div>
                )
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
}

/* =========================================================
   SCHEDULE
========================================================= */

function SchedulePage() {
  const [candidates, setCandidates] = useState([]);
  const [selectedCandidates, setSelectedCandidates] =
    useState([]);
  const [interviewers, setInterviewers] =
    useState([
      "Interviewer 1",
      "Interviewer 2",
      "Interviewer 3",
    ]);
  const [newInterviewer, setNewInterviewer] =
    useState("");
  const [date, setDate] = useState("");
  const [startTime, setStartTime] =
    useState("10:00");
  const [duration, setDuration] =
    useState("30");
  const [assignments, setAssignments] =
    useState([]);
  const [loading, setLoading] =
    useState(false);
  const [error, setError] =
    useState("");

  useEffect(() => {
    const loadCandidates = async () => {
      try {
        const data =
          await apiFetch("/candidates");

        const list =
          Array.isArray(data.candidates)
            ? data.candidates
            : [];

        setCandidates(list);

        setSelectedCandidates(
          list.map(
            (candidate) =>
              candidate.resumeFile
          )
        );
      } catch (err) {
        setError(err.message);
      }
    };

    loadCandidates();
  }, []);

  const toggleCandidate = (resumeFile) => {
    setSelectedCandidates((current) =>
      current.includes(resumeFile)
        ? current.filter(
            (item) => item !== resumeFile
          )
        : [...current, resumeFile]
    );
  };

  const addInterviewer = () => {
    const name = newInterviewer.trim();

    if (!name) return;

    if (
      interviewers.some(
        (item) =>
          item.toLowerCase() ===
          name.toLowerCase()
      )
    ) {
      setNewInterviewer("");
      return;
    }

    setInterviewers((current) => [
      ...current,
      name,
    ]);

    setNewInterviewer("");
  };

  const removeInterviewer = (name) => {
    setInterviewers((current) =>
      current.filter((item) => item !== name)
    );
  };

  const assignInterviews = async () => {
    if (selectedCandidates.length === 0) {
      setError(
        "Select at least one candidate."
      );
      return;
    }

    if (interviewers.length === 0) {
      setError(
        "Add at least one interviewer."
      );
      return;
    }

    try {
      setLoading(true);
      setError("");
      setAssignments([]);

      const selected = candidates.filter(
        (candidate) =>
          selectedCandidates.includes(
            candidate.resumeFile
          )
      );

      /*
       * Internal matching data.
       * The recruiter does not configure or see this.
       */
      const allSkills = [
        ...new Set(
          selected.flatMap(
            (candidate) =>
              candidate.skills || []
          )
        ),
      ];

      const request = {
        candidates: selected.map(
          (candidate) => ({
            name: candidate.name,
            resumeFile:
              candidate.resumeFile,
          })
        ),
        interviewers:
          interviewers.map((name) => ({
            name,
            skills: allSkills,
          })),
      };

      const data =
        await apiFetch("/schedule", {
          method: "POST",
          headers: {
            "Content-Type":
              "application/json",
          },
          body: JSON.stringify(request),
        });

      const generatedAssignments =
        Array.isArray(data.assignments)
          ? data.assignments
          : [];

      const enriched =
        generatedAssignments.map(
          (assignment, index) => ({
            ...assignment,
            date:
              date ||
              new Date()
                .toISOString()
                .slice(0, 10),
            time: calculateSlotTime(
              startTime,
              index,
              Number(duration)
            ),
          })
        );

      setAssignments(enriched);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-shell">
      <div className="page-heading">
        <p className="eyebrow">INTERVIEW WORKFLOW</p>

        <h1>Schedule Interviews</h1>

        <p>
          Select candidates, prepare the interview pool,
          and let TalentFlow assign the available slots.
        </p>
      </div>

      {error && <ErrorBanner message={error} />}

      <div className="schedule-layout">
        <div className="panel schedule-main-panel">
          <div className="panel-header">
            <div>
              <h2>Candidates</h2>
              <p>
                Choose the candidates who should receive
                an interview slot.
              </p>
            </div>

            <span className="selection-count">
              {selectedCandidates.length} selected
            </span>
          </div>

          <div className="schedule-candidate-list">
            {candidates.map((candidate) => {
              const selected =
                selectedCandidates.includes(
                  candidate.resumeFile
                );

              return (
                <button
                  className={
                    selected
                      ? "schedule-candidate selected"
                      : "schedule-candidate"
                  }
                  key={candidate.resumeFile}
                  onClick={() =>
                    toggleCandidate(
                      candidate.resumeFile
                    )
                  }
                >
                  <div className="schedule-check">
                    {selected && (
                      <Check size={14} />
                    )}
                  </div>

                  <div className="mini-avatar">
                    {getInitials(
                      candidate.name
                    )}
                  </div>

                  <div className="schedule-candidate-info">
                    <strong>
                      {candidate.name}
                    </strong>

                    <span>
                      {candidate.email ||
                        "No email"}
                    </span>
                  </div>

                  <ChevronRight size={16} />
                </button>
              );
            })}
          </div>
        </div>

        <div className="schedule-side">
          <div className="panel">
            <div className="panel-header">
              <div>
                <h2>Interview Details</h2>
                <p>Set the available interview window.</p>
              </div>
            </div>

            <div className="form-grid">
              <div className="form-field">
                <label>Date</label>

                <input
                  type="date"
                  value={date}
                  onChange={(event) =>
                    setDate(
                      event.target.value
                    )
                  }
                />
              </div>

              <div className="form-field">
                <label>Start Time</label>

                <input
                  type="time"
                  value={startTime}
                  onChange={(event) =>
                    setStartTime(
                      event.target.value
                    )
                  }
                />
              </div>

              <div className="form-field full-width">
                <label>Interview Duration</label>

                <select
                  value={duration}
                  onChange={(event) =>
                    setDuration(
                      event.target.value
                    )
                  }
                >
                  <option value="20">
                    20 minutes
                  </option>

                  <option value="30">
                    30 minutes
                  </option>

                  <option value="45">
                    45 minutes
                  </option>

                  <option value="60">
                    60 minutes
                  </option>
                </select>
              </div>
            </div>
          </div>

          <div className="panel">
            <div className="panel-header">
              <div>
                <h2>Interview Pool</h2>
                <p>
                  Add the interviewers who are available.
                </p>
              </div>
            </div>

            <div className="interviewer-add">
              <input
                value={newInterviewer}
                onChange={(event) =>
                  setNewInterviewer(
                    event.target.value
                  )
                }
                onKeyDown={(event) => {
                  if (event.key === "Enter") {
                    addInterviewer();
                  }
                }}
                placeholder="Interviewer name"
              />

              <button
                className="outline-button"
                onClick={addInterviewer}
              >
                Add
              </button>
            </div>

            <div className="interviewer-list">
              {interviewers.map((name) => (
                <div
                  className="interviewer-row"
                  key={name}
                >
                  <div className="interviewer-avatar">
                    {getInitials(name)}
                  </div>

                  <span>{name}</span>

                  <button
                    className="remove-interviewer"
                    onClick={() =>
                      removeInterviewer(
                        name
                      )
                    }
                  >
                    <X size={15} />
                  </button>
                </div>
              ))}
            </div>

            <button
              className="filled-button assign-button"
              onClick={assignInterviews}
              disabled={loading}
            >
              {loading ? (
                <>
                  <LoaderCircle
                    size={17}
                    className="spin"
                  />
                  Assigning...
                </>
              ) : (
                <>
                  <CalendarDays size={17} />
                  Assign Interviews
                </>
              )}
            </button>
          </div>
        </div>
      </div>

      {assignments.length > 0 && (
        <div className="panel assignments-panel">
          <div className="panel-header">
            <div>
              <h2>Interview Schedule</h2>
              <p>
                {assignments.length} interview
                {assignments.length !== 1
                  ? "s"
                  : ""} assigned.
              </p>
            </div>

            <span className="success-pill">
              <CheckCircle2 size={14} />
              Scheduled
            </span>
          </div>

          <div className="assignment-grid">
            {assignments.map(
              (assignment, index) => (
                <div
                  className="assignment-card"
                  key={`${assignment.candidate}-${index}`}
                >
                  <div className="assignment-date">
                    <CalendarDays size={16} />
                    {assignment.date}
                  </div>

                  <h3>
                    {assignment.candidate}
                  </h3>

                  <div className="assignment-details">
                    <span>
                      <UserCheck size={15} />
                      {assignment.interviewer}
                    </span>

                    <span>
                      <Clock3 size={15} />
                      {assignment.time}
                    </span>
                  </div>
                </div>
              )
            )}
          </div>
        </div>
      )}
    </div>
  );
}

/* =========================================================
   OPTIMIZATION
========================================================= */

function OptimizationPage() {
  const [query, setQuery] = useState("");
  const [limit, setLimit] = useState("5");
  const [results, setResults] = useState([]);
  const [totalCandidates, setTotalCandidates] =
    useState(0);
  const [loading, setLoading] =
    useState(false);
  const [error, setError] = useState("");

  const optimize = async () => {
    try {
      setLoading(true);
      setError("");

      const data = await apiFetch("/optimize", {
        method: "POST",
        headers: {
          "Content-Type":
            "application/json",
        },
        body: JSON.stringify({
          query,
          limit: Number(limit),
        }),
      });

      setResults(
        Array.isArray(data.results)
          ? data.results
          : []
      );

      setTotalCandidates(
        data.totalCandidates || 0
      );
    } catch (err) {
      setError(err.message);
      setResults([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-shell">
      <div className="page-heading">
        <p className="eyebrow">
          CANDIDATE SELECTION
        </p>

        <h1>Candidate Optimization</h1>

        <p>
          Narrow a large candidate pool into a focused
          shortlist using the requirements of the role.
        </p>
      </div>

      {error && <ErrorBanner message={error} />}

      <div className="panel optimization-controls">
        <div className="optimization-query">
          <label>Role requirements</label>

          <div className="search-large compact">
            <Search size={18} />

            <input
              value={query}
              onChange={(event) =>
                setQuery(event.target.value)
              }
              placeholder="e.g. Python Machine Learning SQL"
            />
          </div>
        </div>

        <div className="optimization-limit">
          <label>Shortlist size</label>

          <select
            value={limit}
            onChange={(event) =>
              setLimit(event.target.value)
            }
          >
            <option value="3">3 candidates</option>
            <option value="5">5 candidates</option>
            <option value="10">10 candidates</option>
            <option value="15">15 candidates</option>
          </select>
        </div>

        <button
          className="filled-button"
          onClick={optimize}
          disabled={loading}
        >
          {loading ? (
            <>
              <LoaderCircle
                size={16}
                className="spin"
              />
              Processing
            </>
          ) : (
            <>
              <Target size={16} />
              Build Shortlist
            </>
          )}
        </button>
      </div>

      {results.length > 0 && (
        <>
          <div className="optimization-summary">
            <div>
              <span>Candidate pool</span>
              <strong>{totalCandidates}</strong>
            </div>

            <div>
              <span>Shortlisted</span>
              <strong>{results.length}</strong>
            </div>

            <div>
              <span>Role focus</span>
              <strong>
                {query || "General profile"}
              </strong>
            </div>
          </div>

          <div className="optimization-grid">
            {results.map((item, index) => (
              <div
                className="panel optimized-candidate"
                key={
                  item.resumeFile ||
                  `${item.email}-${index}`
                }
              >
                <div className="optimized-rank">
                  #{index + 1}
                </div>

                <div className="optimized-header">
                  <div className="candidate-card-avatar">
                    {getInitials(item.name)}
                  </div>

                  <div>
                    <h3>{item.name}</h3>
                    <p>{item.email || "—"}</p>
                  </div>
                </div>

                <div className="optimized-score">
                  <span>Profile relevance</span>
                  <strong>{item.score}%</strong>
                </div>

                <div className="score-track">
                  <div
                    className="score-fill"
                    style={{
                      width: `${Math.min(
                        100,
                        Math.max(0, item.score)
                      )}%`,
                    }}
                  />
                </div>

                <div className="candidate-card-skills">
                  {(item.skills || []).map(
                    (skill) => (
                      <span key={skill}>
                        {skill}
                      </span>
                    )
                  )}
                </div>

                <div className="optimized-experience">
                  <Clock3 size={15} />
                  {formatExperience(
                    item.experience
                  )}
                </div>
              </div>
            ))}
          </div>
        </>
      )}

      {!loading &&
        results.length === 0 && (
          <div className="optimization-empty">
            <Target size={32} />

            <h2>Build your shortlist</h2>

            <p>
              Enter the requirements for a role and choose
              how many candidates you want to advance.
            </p>
          </div>
        )}
    </div>
  );
}

/* =========================================================
   SETTINGS
========================================================= */

function SettingsPage() {
  const [settings, setSettings] = useState(() => {
    try {
      const stored =
        localStorage.getItem(
          "talentflow-settings"
        );

      return stored
        ? JSON.parse(stored)
        : {
            interviewDuration: "30",
            duplicateDetection: true,
            notifications: true,
            compactTables: false,
          };
    } catch {
      return {
        interviewDuration: "30",
        duplicateDetection: true,
        notifications: true,
        compactTables: false,
      };
    }
  });

  const [saved, setSaved] = useState(false);

  const updateSetting = (key, value) => {
    setSettings((current) => ({
      ...current,
      [key]: value,
    }));

    setSaved(false);
  };

  const saveSettings = () => {
    localStorage.setItem(
      "talentflow-settings",
      JSON.stringify(settings)
    );

    setSaved(true);

    setTimeout(() => {
      setSaved(false);
    }, 2200);
  };

  return (
    <div className="page-shell settings-page">
      <div className="page-heading">
        <p className="eyebrow">WORKSPACE SETTINGS</p>

        <h1>Settings</h1>

        <p>
          Configure how your TalentFlow workspace behaves.
        </p>
      </div>

      <div className="settings-grid">
        <div className="panel settings-section">
          <div className="settings-section-heading">
            <div className="settings-icon">
              <UserRound size={18} />
            </div>

            <div>
              <h2>Recruiter Profile</h2>
              <p>Your workspace identity.</p>
            </div>
          </div>

          <div className="settings-profile">
            <div className="large-avatar">Y</div>

            <div>
              <strong>Recruiter</strong>
              <span>TalentFlow workspace</span>
            </div>
          </div>
        </div>

        <div className="panel settings-section">
          <div className="settings-section-heading">
            <div className="settings-icon">
              <CalendarDays size={18} />
            </div>

            <div>
              <h2>Interview Preferences</h2>
              <p>Default scheduling behavior.</p>
            </div>
          </div>

          <div className="setting-row">
            <div>
              <strong>Default interview duration</strong>
              <span>
                Used when creating interview schedules.
              </span>
            </div>

            <select
              value={settings.interviewDuration}
              onChange={(event) =>
                updateSetting(
                  "interviewDuration",
                  event.target.value
                )
              }
            >
              <option value="20">20 minutes</option>
              <option value="30">30 minutes</option>
              <option value="45">45 minutes</option>
              <option value="60">60 minutes</option>
            </select>
          </div>
        </div>

        <div className="panel settings-section">
          <div className="settings-section-heading">
            <div className="settings-icon">
              <ShieldCheck size={18} />
            </div>

            <div>
              <h2>Resume Processing</h2>
              <p>Control resume quality checks.</p>
            </div>
          </div>

          <ToggleSetting
            title="Duplicate detection"
            description="Check uploaded resumes for repeated files."
            enabled={settings.duplicateDetection}
            onChange={(value) =>
              updateSetting(
                "duplicateDetection",
                value
              )
            }
          />
        </div>

        <div className="panel settings-section">
          <div className="settings-section-heading">
            <div className="settings-icon">
              <Settings size={18} />
            </div>

            <div>
              <h2>Workspace Preferences</h2>
              <p>Personalize your recruiter workspace.</p>
            </div>
          </div>

          <ToggleSetting
            title="Notifications"
            description="Show important workflow notifications."
            enabled={settings.notifications}
            onChange={(value) =>
              updateSetting(
                "notifications",
                value
              )
            }
          />

          <ToggleSetting
            title="Compact candidate tables"
            description="Use denser candidate list rows."
            enabled={settings.compactTables}
            onChange={(value) =>
              updateSetting(
                "compactTables",
                value
              )
            }
          />
        </div>
      </div>

      <div className="settings-save-bar">
        {saved && (
          <span className="saved-message">
            <CheckCircle2 size={15} />
            Settings saved
          </span>
        )}

        <button
          className="filled-button"
          onClick={saveSettings}
        >
          <Check size={16} />
          Save Settings
        </button>
      </div>
    </div>
  );
}

/* =========================================================
   SHARED COMPONENTS
========================================================= */

function StatCard({
  number,
  label,
  icon: Icon,
}) {
  return (
    <div className="stat-card">
      <div>
        <strong>{number}</strong>
        <span>{label}</span>
      </div>

      <div className="stat-icon">
        <Icon size={18} strokeWidth={1.8} />
      </div>
    </div>
  );
}

function Activity({
  icon: Icon,
  title,
  text,
}) {
  return (
    <div className="activity">
      <div className="activity-icon">
        <Icon size={18} strokeWidth={1.8} />
      </div>

      <div>
        <strong>{title}</strong>
        <p>{text}</p>
      </div>
    </div>
  );
}

function ActionButton({
  icon: Icon,
  label,
  onClick,
}) {
  return (
    <button
      className="action-button"
      onClick={onClick}
    >
      <span className="action-button-icon">
        <Icon size={17} />
      </span>

      <span>{label}</span>

      <ArrowUpRight size={15} />
    </button>
  );
}

function InfoCard({
  icon: Icon,
  title,
  text,
}) {
  return (
    <div className="upload-info-card">
      <div className="upload-info-icon">
        <Icon size={20} />
      </div>

      <div>
        <strong>{title}</strong>
        <p>{text}</p>
      </div>
    </div>
  );
}

function MetricCard({
  title,
  value,
  icon: Icon,
}) {
  return (
    <div className="metric-card">
      <div className="metric-icon">
        <Icon size={17} />
      </div>

      <span>{title}</span>

      <strong>{value}</strong>
    </div>
  );
}

function ToggleSetting({
  title,
  description,
  enabled,
  onChange,
}) {
  return (
    <div className="setting-row">
      <div>
        <strong>{title}</strong>
        <span>{description}</span>
      </div>

      <button
        className={
          enabled
            ? "toggle active"
            : "toggle"
        }
        onClick={() => onChange(!enabled)}
        aria-label={title}
      >
        <span />
      </button>
    </div>
  );
}

function ErrorBanner({ message }) {
  return (
    <div className="error-banner">
      <AlertTriangle size={17} />
      <span>{message}</span>
    </div>
  );
}

function LoadingState({ text }) {
  return (
    <div className="loading-state">
      <LoaderCircle
        size={24}
        className="spin"
      />
      <span>{text}</span>
    </div>
  );
}

function EmptyState({
  icon: Icon,
  title,
  text,
}) {
  return (
    <div className="empty-state">
      <div className="empty-icon">
        <Icon size={27} />
      </div>

      <h2>{title}</h2>

      <p>{text}</p>
    </div>
  );
}

/* =========================================================
   HELPERS
========================================================= */

function getInitials(name) {
  if (!name) return "?";

  const parts = name
    .trim()
    .split(/\s+/)
    .filter(Boolean);

  if (parts.length === 1) {
    return parts[0]
      .slice(0, 2)
      .toUpperCase();
  }

  return (
    parts[0][0] +
    parts[parts.length - 1][0]
  ).toUpperCase();
}

function formatExperience(value) {
  const number = Number(value);

  if (!Number.isFinite(number) || number <= 0) {
    return "Experience not detected";
  }

  return `${number} ${
    number === 1 ? "year" : "years"
  }`;
}

function formatFileSize(bytes) {
  if (bytes < 1024) {
    return `${bytes} B`;
  }

  if (bytes < 1024 * 1024) {
    return `${(bytes / 1024).toFixed(1)} KB`;
  }

  return `${(
    bytes /
    (1024 * 1024)
  ).toFixed(1)} MB`;
}

function calculateSlotTime(
  startTime,
  index,
  duration
) {
  const [hours, minutes] =
    startTime
      .split(":")
      .map(Number);

  const totalMinutes =
    hours * 60 +
    minutes +
    index * duration;

  const finalHours =
    Math.floor(
      totalMinutes / 60
    ) % 24;

  const finalMinutes =
    totalMinutes % 60;

  return `${String(finalHours).padStart(
    2,
    "0"
  )}:${String(finalMinutes).padStart(
    2,
    "0"
  )}`;
}

export default App;
